package app.suhocki.mybooks.di.provider.database

import app.suhocki.mybooks.data.database.BooksDatabase
import app.suhocki.mybooks.data.database.dao.BookDao
import javax.inject.Inject
import javax.inject.Provider

class BookDaoProvider @Inject constructor(
    private val booksDatabase: BooksDatabase
) : Provider<BookDao> {

    override fun get() = booksDatabase.bookDao()
}