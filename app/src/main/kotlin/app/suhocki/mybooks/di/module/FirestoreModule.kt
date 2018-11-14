package app.suhocki.mybooks.di.module

import app.suhocki.mybooks.data.firestore.FirestoreRepository
import app.suhocki.mybooks.di.Firestore
import app.suhocki.mybooks.domain.repository.BooksRepository
import app.suhocki.mybooks.domain.repository.InfoRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import toothpick.config.Module

class FirestoreModule : Module() {
    init {
        bind(FirebaseFirestore::class.java)
            .toProviderInstance {
                FirebaseFirestore.getInstance().apply {
                    firestoreSettings = FirebaseFirestoreSettings.Builder()
                        .setTimestampsInSnapshotsEnabled(true)
                        .build()
                }
            }
            .providesSingletonInScope()

        bind(BooksRepository::class.java)
            .withName(Firestore::class.java)
            .to(FirestoreRepository::class.java)
            .singletonInScope()

        bind(InfoRepository::class.java)
            .withName(Firestore::class.java)
            .to(FirestoreRepository::class.java)
            .singletonInScope()
    }
}