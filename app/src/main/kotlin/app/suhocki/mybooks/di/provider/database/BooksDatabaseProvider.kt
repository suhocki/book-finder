package app.suhocki.mybooks.di.provider.database

import android.arch.persistence.room.Room
import app.suhocki.mybooks.BuildConfig
import android.content.Context
import app.suhocki.mybooks.data.room.BooksDatabase
import javax.inject.Inject
import javax.inject.Provider

class BooksDatabaseProvider @Inject constructor(
    private val context: Context
) : Provider<BooksDatabase> {

    override fun get(): BooksDatabase =
        Room.databaseBuilder(
            context,
            BooksDatabase::class.java,
            BuildConfig.DATABASE_FILE_NAME
        ).fallbackToDestructiveMigration().build()
}