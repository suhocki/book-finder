package app.suhocki.mybooks.di.provider.database

import app.suhocki.mybooks.data.room.BooksDatabase
import app.suhocki.mybooks.data.room.dao.AuthorStatisticsDao
import javax.inject.Inject
import javax.inject.Provider

class AuthorStatisticsDaoProvider @Inject constructor(
    private val booksDatabase: BooksDatabase
) : Provider<AuthorStatisticsDao> {

    override fun get() = booksDatabase.authorStatisticsDao()
}