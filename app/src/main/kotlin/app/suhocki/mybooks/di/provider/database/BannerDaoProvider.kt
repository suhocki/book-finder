package app.suhocki.mybooks.di.provider.database

import app.suhocki.mybooks.data.room.BooksDatabase
import app.suhocki.mybooks.data.room.dao.BannerDao
import javax.inject.Inject
import javax.inject.Provider

class BannerDaoProvider @Inject constructor(
    private val booksDatabase: BooksDatabase
) : Provider<BannerDao> {

    override fun get() = booksDatabase.bannerDao()
}