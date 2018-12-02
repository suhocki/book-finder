package app.suhocki.mybooks.data.firestore

import app.suhocki.mybooks.data.firestore.entity.FirestoreCategory
import app.suhocki.mybooks.data.mapper.Mapper
import app.suhocki.mybooks.di.provider.CatalogRequestFactoryProvider
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.presentation.base.paginator.PaginationView
import app.suhocki.mybooks.ui.base.entity.UiItem
import app.suhocki.mybooks.ui.catalog.entity.UiCategory
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import javax.inject.Inject

class FirestoreObserver @Inject constructor(
    private val viewState: PaginationView<UiItem>,
    private val firestore: FirebaseFirestore,
    private val mapper: Mapper,
    private val currentData: MutableList<UiItem>
) {
    private val savedSnapshots = mutableListOf<DocumentSnapshot>()
    private var categoriesListener: ListenerRegistration? = null

    fun observeCategories(offset: Int, limit: Int): List<Category> {
        var data = mutableListOf<FirestoreCategory>()
        var needReturnData = true
        dispose()

        configureCurrentPage(offset, limit)
            .addSnapshotListener { snapshot, _ ->
                updatePaginatedData(savedSnapshots, snapshot!!.documents, offset, limit)

                data = snapshot.toObjects(FirestoreCategory::class.java)

                if (needReturnData) needReturnData = false
                else {
                    val newPartOfData = data.map { mapper.map<UiCategory>(it) }
                    val oldTrigger = currentData.find { it.isNextPageTrigger }
                    val oldTriggerIndex =
                        if (oldTrigger != null) currentData.indexOf(oldTrigger)
                        else -1

                    updatePaginatedData(currentData, newPartOfData, offset, limit)

                    val newTrigger = currentData.find { it.isNextPageTrigger }
                    val newTriggerIndex =
                        if (oldTrigger != null) currentData.indexOf(newTrigger)
                        else -1

                    if (oldTriggerIndex != newTriggerIndex) {
                        setNextPageTrigger(currentData)
                    }

                    viewState.showData(currentData)
                }
            }/*.apply { categoriesListener = this }*/

        while (needReturnData) {
        }

        return data
    }

    private fun configureCurrentPage(offset: Int, limit: Int): Query {
        return firestore.collection(FirestoreRepository.CATEGORIES)
            .let { fb -> savedSnapshots.getOrNull(offset - 1)?.let { fb.startAfter(it) } ?: fb }
            .let { fb ->
                if (savedSnapshots.size < offset && savedSnapshots.isNotEmpty()) {
                    fb.startAfter(savedSnapshots.last())
                } else fb
            }
            .limit(limit.toLong())
    }

    fun dispose() = categoriesListener?.remove()

    private fun <T> updatePaginatedData(
        oldData: MutableList<T>,
        newPartOfData: List<T>,
        offset: Int,
        limit: Int
    ) {
        if (oldData.isEmpty()) {
            oldData.addAll(newPartOfData)
            return
        }

        val from =
            if (offset <= oldData.lastIndex) offset
            else oldData.size

        val to =
            if (offset + limit <= oldData.lastIndex) offset + limit
            else oldData.size

        oldData.removeAll(oldData.subList(from, to).toList())
        oldData.addAll(from, newPartOfData)
    }

    private fun setNextPageTrigger(list: List<UiItem>) = with(list) {
        val nextPageTriggerPosition =
            if (size > CatalogRequestFactoryProvider.TRIGGER_OFFSET) size - CatalogRequestFactoryProvider.TRIGGER_OFFSET
            else lastIndex

        list.getOrNull(nextPageTriggerPosition)?.let { it.isNextPageTrigger = true }
    }
}