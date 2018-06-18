package app.suhocki.mybooks.di.provider.database

import app.suhocki.mybooks.data.database.BooksDatabase
import app.suhocki.mybooks.data.database.dao.StatusStatisticsDao
import javax.inject.Inject
import javax.inject.Provider

class StatusStatisticsDaoProvider @Inject constructor(
    private val booksDatabase: BooksDatabase
) : Provider<StatusStatisticsDao> {

    override fun get() = booksDatabase.statusStatisticsDao()
}