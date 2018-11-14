package app.suhocki.mybooks.di.provider.database

import app.suhocki.mybooks.data.room.BooksDatabase
import app.suhocki.mybooks.data.room.dao.ShopInfoDao
import javax.inject.Inject
import javax.inject.Provider

class ShopInfoDaoProvider @Inject constructor(
    private val booksDatabase: BooksDatabase
) : Provider<ShopInfoDao> {

    override fun get() = booksDatabase.shopInfoDao()
}