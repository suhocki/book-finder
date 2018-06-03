package app.suhocki.mybooks.di.provider

import app.suhocki.mybooks.data.database.BooksDatabase
import app.suhocki.mybooks.data.database.dao.YearStatisticsDao
import javax.inject.Inject
import javax.inject.Provider

class YearStatisticsDaoProvider @Inject constructor(
    private val booksDatabase: BooksDatabase
) : Provider<YearStatisticsDao> {

    override fun get() = booksDatabase.yearStatisticsDao()
}