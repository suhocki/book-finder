package app.suhocki.mybooks.di.provider.database

import app.suhocki.mybooks.data.room.BooksDatabase
import app.suhocki.mybooks.data.room.dao.YearStatisticsDao
import javax.inject.Inject
import javax.inject.Provider

class YearStatisticsDaoProvider @Inject constructor(
    private val booksDatabase: BooksDatabase
) : Provider<YearStatisticsDao> {

    override fun get() = booksDatabase.yearStatisticsDao()
}