package app.suhocki.mybooks.domain.repository

import android.arch.persistence.db.SupportSQLiteQuery
import app.suhocki.mybooks.data.room.entity.BookEntity
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.ui.base.entity.UploadControlEntity

interface BooksRepository {
    fun getCategories(): List<Category> =
        throw NotImplementedError()

    fun addCategories(categories: List<Category>)

    fun getCategoryById(id: String): Category =
        throw NotImplementedError()

    fun getBooks(): List<Book> =
        throw NotImplementedError()

    fun addBooks(books: List<Book>, uploadControl: UploadControlEntity? = null)

    fun getBookById(id: String): Book =
        throw NotImplementedError()

    fun getBooksFor(categoryId: String): List<Book> =
        throw NotImplementedError()

    fun search(text: String): List<BookEntity>

    fun filter(query: SupportSQLiteQuery): List<BookEntity>
}