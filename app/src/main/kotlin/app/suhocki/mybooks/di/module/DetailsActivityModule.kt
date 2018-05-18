package app.suhocki.mybooks.di.module

import app.suhocki.mybooks.domain.model.Book
import toothpick.config.Module

class DetailsActivityModule(book: Book) : Module() {
    init {
        bind(Book::class.java).toInstance(book)
    }
}