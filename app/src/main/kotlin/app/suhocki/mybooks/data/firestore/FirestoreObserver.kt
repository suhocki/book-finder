package app.suhocki.mybooks.data.firestore

import app.suhocki.mybooks.data.error.ErrorHandler
import app.suhocki.mybooks.data.mapper.Mapper
import app.suhocki.mybooks.di.FirestoreCollection
import app.suhocki.mybooks.domain.ListTools
import app.suhocki.mybooks.presentation.base.paginator.Paginator
import app.suhocki.mybooks.ui.base.eventbus.ActiveConnectionsCountEvent
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

class FirestoreObserver @Inject constructor(
    @FirestoreCollection private val collectionName: String,
    private val firestore: FirebaseFirestore,
    val mapper: Mapper,
    private val listTools: ListTools,
    private val errorHandler: ErrorHandler
) {

    private val allSnapshots = mutableListOf<DocumentSnapshot>()
    private var observers = mutableListOf<ListenerRegistration>()

    var onPageChanged: ((Int) -> Unit)? = null

    private val onNewSnapshotListeners = mutableListOf<(QuerySnapshot, Int, Int) -> Unit>()

    fun observePage(offset: Int, limit: Int): QuerySnapshot {
        var snapshot: QuerySnapshot? = null

        val observer = configureCurrentPage(offset, limit)
            .addSnapshotListener { newSnapshot, firestoreException ->
                if (newSnapshot == null) {
                    errorHandler.handleError(firestoreException!!)
                    return@addSnapshotListener
                }
                listTools.updatePageData(allSnapshots, newSnapshot.documents, offset, limit)
                if (snapshot == null) {
                    snapshot = newSnapshot
                    return@addSnapshotListener
                }
                onNewSnapshotListeners.forEach {
                    it.invoke(newSnapshot, offset, limit)
                }
            }
        while (snapshot == null) {
        }
        observers.add(observer)
        sendObserversCount()

        return snapshot!!
    }

    fun addOnNewSnapshotListener(listener: (QuerySnapshot, Int, Int) -> Unit) {
        onNewSnapshotListeners.add(listener)
    }

    fun sendObserversCount() {
        onPageChanged?.invoke(observers.size)
        EventBus.getDefault().postSticky(ActiveConnectionsCountEvent(observers.size))
    }

    fun getResubscribedData(
        disposedCount: Int,
        offset: Int,
        limit: Int
    ): List<QuerySnapshot> {
        val querySnapshots = mutableListOf<QuerySnapshot>()

        for (index in 0 until disposedCount) {
            val newOffset = offset + index * Paginator.LIMIT
            val pageQuerySnapshot = observePage(newOffset, limit)

            querySnapshots.add(pageQuerySnapshot)
            if (pageQuerySnapshot.documents.size < limit) {
                break
            }
        }
        return querySnapshots
    }

    fun removeSnapshots(removeFromIndex: Int) {
        allSnapshots.subList(removeFromIndex, allSnapshots.size).clear()
    }

    private fun configureCurrentPage(offset: Int, limit: Int) =
        firestore.collection(collectionName)
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