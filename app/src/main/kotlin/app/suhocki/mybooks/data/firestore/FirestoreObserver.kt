package app.suhocki.mybooks.data.firestore

import app.suhocki.mybooks.di.ErrorReceiver
import app.suhocki.mybooks.presentation.base.paginator.Paginator
import app.suhocki.mybooks.replaceInRange
import app.suhocki.mybooks.ui.base.eventbus.ActiveConnectionsCountEvent
import com.google.firebase.firestore.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.doAsync
import javax.inject.Inject

class FirestoreObserver @Inject constructor(
    private val firestore: FirebaseFirestore,
    @FirestoreCollection private val collectionName: String,
    @ErrorReceiver private val errorReceiver: (Throwable) -> Unit
) {
    private val allSnapshots = mutableListOf<DocumentSnapshot>()
    private var observers = mutableListOf<ListenerRegistration>()

    lateinit var onPageChanged: (Int) -> Unit

    lateinit var onUpdate: (documentUpdates: List<DocumentSnapshot>, offset: Int, limit: Int) -> Unit

    lateinit var onWaitForNext: () -> Unit

    fun observePage(offset: Int, limit: Int): List<DocumentSnapshot> {
        var snapshot: QuerySnapshot? = null

        val observer = configureCurrentPage(offset, limit)
            .addSnapshotListener { newSnapshot, firestoreException ->
                if (newSnapshot == null) {
                    errorReceiver.invoke(firestoreException!!)
                    return@addSnapshotListener
                }

                allSnapshots.replaceInRange(newSnapshot.documents, offset, limit)

                if (snapshot == null) {
                    snapshot = newSnapshot
                } else {
                    handleDocumentUpdates(newSnapshot, offset, limit)
                    sendObserversCount()
                }

            }
        while (snapshot == null) {
        }
        observers.add(observer)
        onPageChanged(observers.size)
        sendObserversCount()

        return snapshot!!.documents
    }

    private fun handleDocumentUpdates(snapshot: QuerySnapshot, offset: Int, limit: Int) {
        val documents = snapshot.documents
        val currentPage = offset / limit
        val lastObservingPage = observers.lastIndex
        val isFullPage = documents.size == limit

        if (!isFullPage && currentPage < lastObservingPage) {
            dispose(offset / limit + 1)
            onPageChanged(observers.size)
        }

        val hasAddedElement =
            snapshot.documentChanges.any { it.type == DocumentChange.Type.ADDED }

        if (isFullPage && currentPage < lastObservingPage && hasAddedElement) {
            val disposedCount = dispose(currentPage + 1)
            val nextPageOffset = offset + limit
            allSnapshots.subList(nextPageOffset, allSnapshots.size).clear()

            resubscribeFrom(nextPageOffset, limit, disposedCount) {
                allSnapshots.addAll(it)
                onWaitForNext()
                onUpdate(it, nextPageOffset, nextPageOffset + disposedCount * limit)
            }
        }

        onUpdate(documents, offset, limit)
    }

    fun sendObserversCount() {
        EventBus.getDefault().postSticky(ActiveConnectionsCountEvent(observers.size))
    }

    private fun resubscribeFrom(
        offset: Int,
        limit: Int,
        pagesCount: Int,
        onDocumentsLoaded: (List<DocumentSnapshot>) -> Unit
    ) = doAsync {
        val documents = mutableListOf<DocumentSnapshot>()
        for (index in 0 until pagesCount) {
            val newOffset = offset + index * Paginator.LIMIT
            val pageDocuments = observePage(newOffset, limit)

            documents.addAll(pageDocuments)
            if (pageDocuments.size < limit) {
                break
            }
        }
        onDocumentsLoaded.invoke(documents)
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

    fun dispose(fromPage: Int = 0) =
        with(observers.subList(fromPage, observers.size)) {
            val subListSize = size
            forEach { it.remove() }
            clear()
            subListSize
        }
}