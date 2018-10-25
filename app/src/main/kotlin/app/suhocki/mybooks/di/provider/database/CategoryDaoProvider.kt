package app.suhocki.mybooks.di.provider.database

import app.suhocki.mybooks.data.room.BooksDatabase
import app.suhocki.mybooks.data.room.dao.CategoryDao
import javax.inject.Inject
import javax.inject.Provider

class CategoryDaoProvider @Inject constructor(
    private val booksDatabase: BooksDatabase
) : Provider<CategoryDao> {

    override fun get() = booksDatabase.categoryDao()
}