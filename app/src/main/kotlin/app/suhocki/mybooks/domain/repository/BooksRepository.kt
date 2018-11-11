package app.suhocki.mybooks.domain.repository

import android.arch.persistence.db.SupportSQLiteQuery
import app.suhocki.mybooks.data.room.entity.BookEntity
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.ui.base.entity.UploadControlEntity

interface BooksRepository {
    fun getCategories(): List<Category>

    fun setCategories(categories: Set<Category>)

    fun getBooks(): List<Book>

    fun setBooks(books: List<Book>, uploadControl: UploadControlEntity? = null)

    fun getBooksFor(category: Category): List<Book>

    fun search(text: String): List<BookEntity>

    fun filter(query: SupportSQLiteQuery): List<BookEntity>
}