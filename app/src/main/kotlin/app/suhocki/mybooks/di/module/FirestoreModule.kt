package app.suhocki.mybooks.di.module

import app.suhocki.mybooks.data.firestore.FirestoreObserver
import app.suhocki.mybooks.data.firestore.FirestoreRepository
import app.suhocki.mybooks.di.Firestore
import app.suhocki.mybooks.domain.repository.BannersRepository
import app.suhocki.mybooks.domain.repository.BooksRepository
import app.suhocki.mybooks.domain.repository.CategoriesRepository
import app.suhocki.mybooks.domain.repository.InfoRepository
import app.suhocki.mybooks.ui.base.entity.UiItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import toothpick.config.Module

class FirestoreModule : Module() {

    private val firebaseFirestore = FirebaseFirestore.getInstance().apply {
        firestoreSettings = FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build()
    }

    init {
        bind(FirebaseFirestore::class.java)
            .toInstance(firebaseFirestore)

        bind(BooksRepository::class.java)
            .withName(Firestore::class.java)
            .to(FirestoreRepository::class.java)

        bind(CategoriesRepository::class.java)
            .withName(Firestore::class.java)
            .to(FirestoreRepository::class.java)

        bind(InfoRepository::class.java)
            .withName(Firestore::class.java)
            .to(FirestoreRepository::class.java)

        bind(BannersRepository::class.java)
            .withName(Firestore::class.java)
            .to(FirestoreRepository::class.java)

        bind(MutableList::class.java)
            .toInstance(mutableListOf<UiItem>())

        bind(FirestoreObserver::class.java)
            .singletonInScope()
    }
}