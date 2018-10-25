package app.suhocki.mybooks.di.provider.database

import app.suhocki.mybooks.data.room.BooksDatabase
import app.suhocki.mybooks.data.room.dao.PriceStatisticsDao
import javax.inject.Inject
import javax.inject.Provider

class PriceStatisticsDaoProvider @Inject constructor(
    private val booksDatabase: BooksDatabase
) : Provider<PriceStatisticsDao> {

    override fun get() = booksDatabase.priceStatisticsDao()
}