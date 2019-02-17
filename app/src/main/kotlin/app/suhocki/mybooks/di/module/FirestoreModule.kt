package app.suhocki.mybooks.di.module

import app.suhocki.mybooks.data.firestore.FirestoreRepository
import app.suhocki.mybooks.di.Firestore
import app.suhocki.mybooks.domain.repository.BannersRepository
import app.suhocki.mybooks.domain.repository.BooksRepository
import app.suhocki.mybooks.domain.repository.InfoRepository
import toothpick.config.Module

class FirestoreModule : Module() {



    init {

        bind(BooksRepository::class.java)
            .withName(Firestore::class.java)
            .to(FirestoreRepository::class.java)

        bind(InfoRepository::class.java)
            .withName(Firestore::class.java)
            .to(FirestoreRepository::class.java)

        bind(BannersRepository::class.java)
            .withName(Firestore::class.java)
            .to(FirestoreRepository::class.java)
    }
}