package app.suhocki.mybooks.di.provider.database

import android.arch.persistence.room.Room
import android.content.Context
import app.suhocki.mybooks.data.database.BooksDatabase
import app.suhocki.mybooks.di.DatabaseFileName
import javax.inject.Inject
import javax.inject.Provider

class BooksDatabaseProvider @Inject constructor(
    private val context: Context,
    @DatabaseFileName private val databaseFileName: String
) : Provider<BooksDatabase> {

    override fun get(): BooksDatabase =
        Room.databaseBuilder(context, BooksDatabase::class.java, databaseFileName)
            .fallbackToDestructiveMigration()
            .build()
}