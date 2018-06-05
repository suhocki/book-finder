package app.suhocki.mybooks.domain.repository

import app.suhocki.mybooks.data.database.entity.BookEntity
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.domain.model.Category

interface BookDatabaseRepository {
    fun getCategories(): List<Category>

    fun setCategories(categories: Set<Category>)

    fun getBooks(): List<Book>

    fun setBooks(books: List<Book>)

    fun getBooksFor(category: Category): List<BookEntity>

    fun search(text: String): List<BookEntity>
}