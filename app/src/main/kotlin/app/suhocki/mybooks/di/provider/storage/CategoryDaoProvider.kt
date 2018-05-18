package app.suhocki.mybooks.di.provider.storage

import app.suhocki.mybooks.data.database.BooksDatabase
import app.suhocki.mybooks.data.database.dao.CategoryDao
import javax.inject.Inject
import javax.inject.Provider

class CategoryDaoProvider @Inject constructor(
    private val booksDatabase: BooksDatabase
) : Provider<CategoryDao> {

    override fun get() = booksDatabase.categoryDao()
}