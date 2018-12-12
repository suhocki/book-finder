package app.suhocki.mybooks.data.firestore

import app.suhocki.mybooks.data.error.ErrorHandler
import app.suhocki.mybooks.data.firestore.entity.FirestoreCategory
import app.suhocki.mybooks.data.mapper.Mapper
import app.suhocki.mybooks.domain.ListTools
import app.suhocki.mybooks.presentation.base.paginator.Paginator
import app.suhocki.mybooks.ui.base.entity.PageProgress
import app.suhocki.mybooks.ui.base.entity.UiItem
import app.suhocki.mybooks.ui.base.eventbus.ActiveConnectionsCountEvent
import app.suhocki.mybooks.ui.catalog.entity.UiCategory
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.doAsync
import javax.inject.Inject

class FirestoreObserver @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val mapper: Mapper,
    private val allData: MutableList<UiItem>,
    private val listTools: ListTools,
    private val errorHandler: ErrorHandler
) {
    private val allSnapshots = mutableListOf<DocumentSnapshot>()
    private var observers = mutableListOf<ListenerRegistration>()
    var onDataUpdated: ((List<UiItem>) -> Unit)? = null
    var onEmptyData: (() -> Unit)? = null
    var onNotEmptyData: (() -> Unit)? = null
    var onCurrentPageChanged: ((Int) -> Unit)? = null

    fun observeCategories(offset: Int, limit: Int): MutableList<UiItem> {

        var pageData = mutableListOf<UiItem>()
        var needReturnData = true

        val observer = configureCurrentPage(offset, limit)
            .addSnapshotListener { snapshot, firestoreException ->

                if (snapshot == null) {
                    errorHandler.handleError(firestoreException!!)
                    return@addSnapshotListener
                }

                pageData = snapshot.toObjects(FirestoreCategory::class.java)
                    .asSequence()
                    .mapIndexed { index, firestoreCategory ->
                        firestoreCategory.id = snapshot.documents[index].id
                        firestoreCategory
                    }.mapTo(mutableListOf()) { mapper.map<UiCategory>(it) }

                listTools.updatePageData(allSnapshots, snapshot.documents, offset, limit)

                if (needReturnData) {
                    allData.removeAll { it is PageProgress }
                    needReturnData = false
                } else {
                    if (pageData.isEmpty() && offset == 0) {
                        allData.clear()
                        dispose(1)
                        onEmptyData?.invoke()
                    }

                    if (pageData.size == limit) {
                        onNotEmptyData?.invoke()
                    }

                    listTools.updatePageData(allData, pageData, offset, limit)

                    if (hasRemovedOrAddedItems(snapshot.documentChanges)) {
                        val disposedCount = dispose((offset + Paginator.PAGE_SIZE) / limit)

                        when {
                            pageData.size == limit && disposedCount == 0 ->
                                listTools.addProgressAndSetTrigger(allData, limit)

                            pageData.size < limit && disposedCount == 0 ->
                                allData.removeAll { it is PageProgress }

                            pageData.size == limit -> {
                                val nextPageOffset = offset + Paginator.PAGE_SIZE
                                allData.subList(nextPageOffset, allData.size).clear()
                                allSnapshots.subList(nextPageOffset, allSnapshots.size).clear()

                                doAsync {
                                    val newData =
                                        getResubscribedData(disposedCount, nextPageOffset, limit)
                                    allData.addAll(newData)
                                    listTools.addProgressAndSetTrigger(allData, limit)
                                    onDataUpdated?.invoke(allData)
                                }
                                return@addSnapshotListener
                            }

                            pageData.isEmpty() ->
                                allData.removeAll { it is PageProgress }
                        }
                    }

                    onDataUpdated?.invoke(allData)
                    EventBus.getDefault().postSticky(ActiveConnectionsCountEvent(observers.size))
                }

                onCurrentPageChanged?.invoke(observers.size)
            }

        while (needReturnData) {
        }

        observers.add(observer)
        onCurrentPageChanged?.invoke(observers.size)

        EventBus.getDefault().postSticky(ActiveConnectionsCountEvent(observers.size))

        return pageData
    }

    private fun getResubscribedData(
        disposedCount: Int,
        offset: Int,
        limit: Int
    ): MutableList<UiItem> {
        val pagesData = mutableListOf<UiItem>()

        for (index in 0 until disposedCount) {
            val newOffset = offset + index * Paginator.PAGE_SIZE
            val pageData = observeCategories(newOffset, limit)

            pagesData.addAll(pageData)
            if (pageData.size < limit) {
                break
            }
        }
        return pagesData.asSequence()
            .map { mapper.map<UiCategory>(it) as UiItem }
            .toMutableList()
    }

    private fun hasRemovedOrAddedItems(
        documentChanges: MutableList<DocumentChange>
    ) = documentChanges.any {
        it.type == DocumentChange.Type.ADDED ||
                it.type == DocumentChange.Type.REMOVED
    }

    private fun configureCurrentPage(
        offset: Int,
        limit: Int
    ) = firestore.collection(FirestoreRepository.CATEGORIES)

        .let { fb ->
            allSnapshots.getOrNull(offset - 1)?.let { fb.startAfter(it) } ?: fb
        }
        .let { fb ->
            if (allSnapshots.size < offset && allSnapshots.isNotEmpty()) {
                fb.startAfter(allSnapshots.last())
            } else fb
        }
        .limit(limit.toLong())

    fun dispose(startPage: Int = 0): Int {
        if (observers.isEmpty()) return 0

        val startFromIndex =
            if (startPage > observers.size) observers.size
            else startPage
        val oldObservers = observers.subList(startFromIndex, observers.size)
        val disposedCount = oldObservers.size
        oldObservers.forEach { it.remove() }
        oldObservers.clear()

        return disposedCount
    }
}