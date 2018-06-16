package app.suhocki.mybooks.di.provider

import app.suhocki.mybooks.data.database.BooksDatabase
import app.suhocki.mybooks.data.database.dao.BannerDao
import javax.inject.Inject
import javax.inject.Provider

class BannerDaoProvider @Inject constructor(
    private val booksDatabase: BooksDatabase
) : Provider<BannerDao> {

    override fun get() = booksDatabase.bannerDao()
}