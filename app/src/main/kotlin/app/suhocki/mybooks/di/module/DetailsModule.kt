package app.suhocki.mybooks.di.module

import app.suhocki.mybooks.di.BookId
import toothpick.config.Module

class DetailsModule(bookId: String) : Module() {
    init {
        bind(String::class.java)
            .withName(BookId::class.java)
            .toInstance(bookId)
    }
}