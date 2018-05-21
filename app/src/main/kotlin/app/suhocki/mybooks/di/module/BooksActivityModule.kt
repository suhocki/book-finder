package app.suhocki.mybooks.di.module

import android.support.v7.widget.LinearLayoutManager
import app.suhocki.mybooks.di.provider.presentation.BookItemCallbackProvider
import app.suhocki.mybooks.di.provider.presentation.BooksDifferProvider
import app.suhocki.mybooks.di.provider.presentation.LinearLayoutManagerProvider
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.presentation.books.adapter.BookItemCallback
import app.suhocki.mybooks.presentation.books.adapter.BooksAdapter
import app.suhocki.mybooks.presentation.books.adapter.BooksDiffer
import toothpick.config.Module

class BooksActivityModule(category: Category) : Module() {
    init {
        bind(Category::class.java).toInstance(category)
        bind(BooksAdapter::class.java).singletonInScope()
        bind(LinearLayoutManager::class.java).toProvider(LinearLayoutManagerProvider::class.java).singletonInScope()
        bind(BooksDiffer::class.java).toProvider(BooksDifferProvider::class.java).singletonInScope()
        bind(BookItemCallback::class.java).toProvider(BookItemCallbackProvider::class.java).singletonInScope()
    }
}