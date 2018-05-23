package app.suhocki.mybooks.domain.repository

import app.suhocki.mybooks.data.database.entity.BookEntity
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.domain.model.Category

interface DatabaseRepository {
    fun getCategories(): List<Category>

    fun saveCategories(categories: Set<Category>)

    fun getBooks(): List<Book>

    fun saveBooks(books: List<Book>)

    fun getBooksBy(category: Category): List<BookEntity>
}