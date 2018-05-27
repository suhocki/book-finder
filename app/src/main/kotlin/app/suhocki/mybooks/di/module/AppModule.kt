package app.suhocki.mybooks.di.module

import android.content.Context
import android.content.SharedPreferences
import app.suhocki.mybooks.BuildConfig
import app.suhocki.mybooks.data.database.BooksDatabase
import app.suhocki.mybooks.data.database.dao.BookDao
import app.suhocki.mybooks.data.database.dao.CategoryDao
import app.suhocki.mybooks.data.error.ErrorHandler
import app.suhocki.mybooks.data.notifier.ComponentNotifier
import app.suhocki.mybooks.data.progress.ProgressHandler
import app.suhocki.mybooks.data.repository.RoomRepository
import app.suhocki.mybooks.data.repository.SharedPreferencesRepository
import app.suhocki.mybooks.di.DatabaseFileName
import app.suhocki.mybooks.di.SharedPreferencesFileName
import app.suhocki.mybooks.di.provider.BookDaoProvider
import app.suhocki.mybooks.di.provider.BooksDatabaseProvider
import app.suhocki.mybooks.di.provider.CategoryDaoProvider
import app.suhocki.mybooks.di.provider.SharedPreferencesProvider
import app.suhocki.mybooks.domain.repository.DatabaseRepository
import app.suhocki.mybooks.domain.repository.SettingsRepository
import toothpick.config.Module

class AppModule(context: Context) : Module() {
    init {
        bind(Context::class.java)
            .toInstance(context)

        bind(String::class.java)
            .withName(DatabaseFileName::class.java)
            .toInstance(BuildConfig.DATABASE_FILE_NAME)

        bind(String::class.java)
            .withName(SharedPreferencesFileName::class.java)
            .toInstance(BuildConfig.SHARED_PREFERENCES_FILE_NAME)

        bind(BooksDatabase::class.java)
            .toProvider(BooksDatabaseProvider::class.java)
            .providesSingletonInScope()

        bind(BookDao::class.java)
            .toProvider(BookDaoProvider::class.java)
            .providesSingletonInScope()

        bind(CategoryDao::class.java)
            .toProvider(CategoryDaoProvider::class.java)
            .providesSingletonInScope()

        bind(DatabaseRepository::class.java)
            .to(RoomRepository::class.java)
            .singletonInScope()

        bind(SharedPreferences::class.java)
            .toProvider(SharedPreferencesProvider::class.java)
            .providesSingletonInScope()

        bind(ErrorHandler::class.java)
            .singletonInScope()

        bind(ProgressHandler::class.java)
            .singletonInScope()

        bind(ComponentNotifier::class.java)
            .singletonInScope()

        bind(SettingsRepository::class.java)
            .to(SharedPreferencesRepository::class.java)
            .singletonInScope()
    }
}