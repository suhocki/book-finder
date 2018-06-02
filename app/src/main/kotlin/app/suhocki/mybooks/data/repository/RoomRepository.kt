package app.suhocki.mybooks.data.repository

import app.suhocki.mybooks.data.database.dao.BookDao
import app.suhocki.mybooks.data.database.dao.CategoryDao
import app.suhocki.mybooks.data.database.entity.BookEntity
import app.suhocki.mybooks.data.database.entity.CategoryEntity
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.repository.DatabaseRepository
import javax.inject.Inject

class RoomRepository @Inject constructor(
    private val bookDao: BookDao,
    private val categoryDao: CategoryDao
) : DatabaseRepository {
    override fun getCategories(): List<Category> {
        return categoryDao.getAll()
    }

    override fun saveCategories(categories: Set<Category>) {
        categoryDao.insertAll(categories.map { it as CategoryEntity }.distinct())
    }

    override fun getBooks(): List<BookEntity> {
        return bookDao.getAll()
    }

    override fun saveBooks(books: List<Book>) {
        bookDao.insertAll(books.map { it as BookEntity })
    }

    override fun getBooksBy(category: Category): List<BookEntity> {
        return bookDao.getAllByCategory(category.name)
    }

    override fun search(text: String): List<BookEntity> {
        return bookDao.find("%$text%")
    }
}