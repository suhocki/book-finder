package app.suhocki.mybooks.di.provider.database

import app.suhocki.mybooks.data.room.BooksDatabase
import app.suhocki.mybooks.data.room.dao.PublisherStatisticsDao
import javax.inject.Inject
import javax.inject.Provider

class PublisherStatisticsDaoProvider @Inject constructor(
    private val booksDatabase: BooksDatabase
) : Provider<PublisherStatisticsDao> {

    override fun get() = booksDatabase.publisherStatisticsDao()
}