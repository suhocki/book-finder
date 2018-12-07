package app.suhocki.mybooks.data.firestore

import app.suhocki.mybooks.data.firestore.entity.FirestoreCategory
import app.suhocki.mybooks.data.mapper.Mapper
import app.suhocki.mybooks.di.provider.CatalogRequestFactoryProvider
import app.suhocki.mybooks.domain.ListTools
import app.suhocki.mybooks.ui.base.entity.PageProgress
import app.suhocki.mybooks.ui.base.entity.UiItem
import app.suhocki.mybooks.ui.catalog.entity.UiCategory
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import org.jetbrains.anko.doAsync
import javax.inject.Inject

class FirestoreObserver @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val mapper: Mapper,
    private val allData: MutableList<UiItem>,
    private val listTools: ListTools
) {
    private val allSnapshots = mutableListOf<DocumentSnapshot>()
    private var observers = mutableListOf<ListenerRegistration>()
    var observersCountListener: ((Int) -> Unit)? = null
    var refreshedDataListener: ((List<UiItem>) -> Unit)? = null

    fun observeCategories(offset: Int, limit: Int): List<FirestoreCategory> {
        var pageData = mutableListOf<FirestoreCategory>()
        var needReturnData = true

        val observer = configureCurrentPage(offset, limit)
            .addSnapshotListener { snapshot, _ ->
                pageData = snapshot!!.toObjects(FirestoreCategory::class.java)

                listTools.updatePageData(allSnapshots, snapshot.documents, offset, limit)

                if (needReturnData) {
                    listTools.removePageProgress(allData)
                    needReturnData = false
                } else {
                    var disposedCount = 0

                    val currentObserverIndex = offset / limit
                    if (hasRemovedOrAddedItems(snapshot.documentChanges)) {
                        val disposeFromIndex = currentObserverIndex + 1
                        disposedCount = dispose(disposeFromIndex)
                    }

                    val uiData = pageData.asSequence()
                        .map { mapper.map<UiCategory>(it) as UiItem }
                        .toMutableList()
                    if (disposedCount == 0) {
                        if (currentObserverIndex == observers.lastIndex &&
                            uiData.size == limit
                        ) {
                            listTools.updatePageData(allData, uiData, offset, limit)
                            listTools.setNextPageTrigger(allData)
                            listTools.addPageProgress(allData)

                            refreshedDataListener?.invoke(allData)
                        }
                    } else {
                        listTools.updatePageData(allData, uiData, offset, limit)
                        reSubscribeFrom(offset + pageData.size, disposedCount)
                    }
                }
            }

        while (needReturnData) {
        }

        if (pageData.isNotEmpty() || offset == 0) observers.add(observer)
        else observer.remove()

        observersCountListener?.invoke(observers.size)
        return pageData
    }

    private fun fixPageTrigger(
        pageData: MutableList<UiItem>,
        offset: Int,
        limit: Int
    ) {
        val oldTriggerIndex = listTools.findTriggerIndex(allData)

        listTools.updatePageData(allData, pageData, offset, limit)

        val newTriggerIndex = listTools.findTriggerIndex(allData)

        if (oldTriggerIndex != newTriggerIndex) listTools.setNextPageTrigger(allData)
    }

    private fun reSubscribeFrom(offset: Int, disposedCount: Int) {
        val limit = CatalogRequestFactoryProvider.ITEMS_PER_PAGE
        val startPage = offset / limit
        val hasPageProgress = allData.lastOrNull() is PageProgress

        allData.subList(offset, allData.size).clear()
        allSnapshots.subList(offset, allSnapshots.size).clear()

        doAsync {
            val pagesData = mutableListOf<FirestoreCategory>()

            repeat(disposedCount) { index ->
                val observerOffset = (startPage + index) * limit
                val pageData = observeCategories(observerOffset, limit)

                if (pageData.isNotEmpty()) pagesData.addAll(pageData)
                else return@repeat
            }
            val uiData = pagesData.asSequence()
                .map { mapper.map<UiCategory>(it) as UiItem }
                .toMutableList()
                .apply {
                    listTools.setNextPageTrigger(this)
                    if (hasPageProgress) listTools.addPageProgress(this)
                }
            fixPageTrigger(uiData, offset, limit)

            refreshedDataListener?.invoke(allData)
        }
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

        val oldObservers = observers.subList(startPage, observers.size)
        val disposedCount = oldObservers.size
        oldObservers.forEach { it.remove() }
        oldObservers.clear()

        return disposedCount
    }
}