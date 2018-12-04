package app.suhocki.mybooks.data.firestore

import app.suhocki.mybooks.data.firestore.entity.FirestoreCategory
import app.suhocki.mybooks.data.mapper.Mapper
import app.suhocki.mybooks.di.provider.CatalogRequestFactoryProvider
import app.suhocki.mybooks.domain.ListTools
import app.suhocki.mybooks.presentation.base.paginator.PaginationView
import app.suhocki.mybooks.ui.base.entity.PageProgress
import app.suhocki.mybooks.ui.base.entity.UiItem
import app.suhocki.mybooks.ui.catalog.entity.UiCategory
import app.suhocki.mybooks.uiThread
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import org.jetbrains.anko.doAsync
import javax.inject.Inject

class FirestoreObserver @Inject constructor(
    private val viewState: PaginationView<UiItem>,
    private val firestore: FirebaseFirestore,
    private val mapper: Mapper,
    private val allData: MutableList<UiItem>,
    private val listTools: ListTools
) {
    private val allSnapshots = mutableListOf<DocumentSnapshot>()
    private var observers = mutableListOf<ListenerRegistration>()

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
                    val uiData = pageData.asSequence()
                        .map { mapper.map<UiCategory>(it) as UiItem }
                        .toMutableList()
                    sendToUi(uiData, offset, limit)

                    if (hasRemovedOrAddedItems(snapshot.documentChanges)) {
                        val startPage = offset / limit
                        val disposedCount = dispose(startPage + 1)
                        val realOffset = offset + pageData.size

                        if (disposedCount > 0) reSubscribeFrom(realOffset, disposedCount)
                    }
                }
            }

        while (needReturnData) {
        }
        if (pageData.isNotEmpty()) observers.add(observer)
        return pageData
    }

    private fun sendToUi(
        pageData: MutableList<UiItem>,
        offset: Int,
        limit: Int
    ) {
        val oldTriggerIndex = listTools.findTriggerIndex(allData)

        listTools.updatePageData(allData, pageData, offset, limit)

        val newTriggerIndex = listTools.findTriggerIndex(allData)

        if (oldTriggerIndex != newTriggerIndex) listTools.setNextPageTrigger(allData)

        uiThread { viewState.showData(allData) }
    }

    private fun reSubscribeFrom(offset: Int, disposedCount: Int) {
        val limit = CatalogRequestFactoryProvider.ITEMS_PER_PAGE
        val startPage = offset / limit
        val hasPageProgress = allData.last() is PageProgress

        allData.subList(offset, allData.size).clear()
        allSnapshots.subList(offset, allSnapshots.size).clear()

        doAsync {
            val pagesData = mutableListOf<FirestoreCategory>()

            repeat(disposedCount) { index ->
                val realOffset = (startPage + index) * limit
                val pageData = observeCategories(realOffset, limit)

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

            sendToUi(uiData, offset, limit)
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
        val oldObservers = observers.subList(startPage, observers.size)
        val disposedCount = oldObservers.size
        oldObservers.forEach { it.remove() }
        oldObservers.clear()

        return disposedCount
    }
}