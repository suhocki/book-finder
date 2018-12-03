package app.suhocki.mybooks.data.firestore

import app.suhocki.mybooks.data.firestore.entity.FirestoreCategory
import app.suhocki.mybooks.data.mapper.Mapper
import app.suhocki.mybooks.domain.ListTools
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.presentation.base.paginator.PaginationView
import app.suhocki.mybooks.ui.base.entity.UiItem
import app.suhocki.mybooks.ui.catalog.entity.UiCategory
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import java.util.*
import javax.inject.Inject

class FirestoreObserver @Inject constructor(
    private val viewState: PaginationView<UiItem>,
    private val firestore: FirebaseFirestore,
    private val mapper: Mapper,
    private val currentData: MutableList<UiItem>,
    private val listTools: ListTools
) {
    private val currentSnapshots = mutableListOf<DocumentSnapshot>()
    private var observers = ArrayDeque<ListenerRegistration>()

    fun observeCategories(offset: Int, limit: Int): List<Category> {
        var newSnapshots = mutableListOf<FirestoreCategory>()
        var needReturnData = true

        val observer = configureCurrentPage(offset, limit)
            .addSnapshotListener { snapshot, _ ->
                newSnapshots = snapshot!!.toObjects(FirestoreCategory::class.java)

                listTools.updatePaginatedData(currentSnapshots, snapshot.documents, offset, limit)

                if (needReturnData) {
                    listTools.removePageProgress(currentData)
                    needReturnData = false
                } else {
                    onDataUpdated(newSnapshots, offset, limit)
                    viewState.showData(currentData)
                }
            }
        observers.add(observer)

        while (needReturnData) {
        }

        return newSnapshots
    }

    private fun onDataUpdated(
        newSnapshots: MutableList<FirestoreCategory>,
        offset: Int,
        limit: Int
    ) {
        val newData = newSnapshots.map { mapper.map<UiCategory>(it) }
        val oldTriggerIndex = listTools.findTriggerIndex(currentData)

        listTools.updatePaginatedData(currentData, newData, offset, limit)

        val newTriggerIndex = listTools.findTriggerIndex(currentData)

        if (oldTriggerIndex != newTriggerIndex) listTools.setNextPageTrigger(currentData)
    }

    private fun configureCurrentPage(offset: Int, limit: Int): Query {
        return firestore.collection(FirestoreRepository.CATEGORIES)
            .let { fb ->
                currentSnapshots.getOrNull(offset - 1)?.let { fb.startAfter(it) } ?: fb
            }
            .let { fb ->
                if (currentSnapshots.size < offset && currentSnapshots.isNotEmpty()) {
                    fb.startAfter(currentSnapshots.last())
                } else fb
            }
            .limit(limit.toLong())
    }

    fun dispose() {
        repeat(observers.size) {
            observers.poll().remove()
        }
    }
}