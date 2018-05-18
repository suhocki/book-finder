package app.suhocki.mybooks.di.provider.presentation

import android.support.v7.recyclerview.extensions.AsyncListDiffer
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.presentation.books.adapter.BookItemCallback
import app.suhocki.mybooks.presentation.books.adapter.BooksAdapter
import app.suhocki.mybooks.presentation.books.adapter.BooksDiffer
import javax.inject.Inject
import javax.inject.Provider

class BooksDifferProvider @Inject constructor(
    private val adapter: BooksAdapter,
    private val itemCallback: BookItemCallback
): Provider<BooksDiffer> {

    override fun get(): BooksDiffer = BooksDiffer().apply {
        set(AsyncListDiffer<Book>(adapter, itemCallback.get()))
    }
}