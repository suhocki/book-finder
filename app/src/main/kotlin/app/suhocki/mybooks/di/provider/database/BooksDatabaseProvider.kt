package app.suhocki.mybooks.di.provider.database

import android.arch.persistence.room.Room
import app.suhocki.mybooks.BuildConfig
import app.suhocki.mybooks.data.context.ContextManager
import app.suhocki.mybooks.data.database.BooksDatabase
import javax.inject.Inject
import javax.inject.Provider

class BooksDatabaseProvider @Inject constructor(
    private val contextManager: ContextManager
) : Provider<BooksDatabase> {

    override fun get(): BooksDatabase =
        Room.databaseBuilder(
            contextManager.currentContext,
            BooksDatabase::class.java,
            BuildConfig.DATABASE_FILE_NAME
        ).fallbackToDestructiveMigration().build()
}