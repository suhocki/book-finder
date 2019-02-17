package app.suhocki.mybooks.di.provider

import app.suhocki.mybooks.data.firestore.FirestoreObserver
import app.suhocki.mybooks.di.ErrorReceiver
import app.suhocki.mybooks.presentation.global.GlobalFirestoreConnectionsController
import com.google.firebase.firestore.Query
import javax.inject.Inject
import javax.inject.Provider

class FirestoreObserverProvider @Inject constructor(
    private val query: Query,
    private val firestoreConnectionsController: GlobalFirestoreConnectionsController,
    @ErrorReceiver private val errorReceiver: (Throwable) -> Unit
) : Provider<FirestoreObserver> {

    override fun get(): FirestoreObserver =
        FirestoreObserver(query, errorReceiver, firestoreConnectionsController)
}