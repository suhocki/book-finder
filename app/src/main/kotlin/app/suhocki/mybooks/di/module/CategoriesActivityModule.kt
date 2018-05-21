package app.suhocki.mybooks.di.module

import android.support.v7.widget.LinearLayoutManager
import app.suhocki.mybooks.di.CategoriesStartFlag
import app.suhocki.mybooks.di.PrimitiveWrapper
import app.suhocki.mybooks.di.provider.presentation.CategoriesDifferProvider
import app.suhocki.mybooks.di.provider.presentation.CategoryItemCallbackProvider
import app.suhocki.mybooks.di.provider.presentation.LinearLayoutManagerProvider
import app.suhocki.mybooks.presentation.categories.adapter.CategoriesAdapter
import app.suhocki.mybooks.presentation.categories.adapter.CategoriesDiffer
import app.suhocki.mybooks.presentation.categories.adapter.CategoryItemCallback
import toothpick.config.Module

class CategoriesActivityModule(startFromNotification: Boolean) : Module() {
    init {
        bind(PrimitiveWrapper::class.java).withName(CategoriesStartFlag::class.java).toInstance(PrimitiveWrapper(startFromNotification))
        bind(CategoriesAdapter::class.java).singletonInScope()
        bind(LinearLayoutManager::class.java).toProvider(LinearLayoutManagerProvider::class.java).singletonInScope()
        bind(CategoriesDiffer::class.java).toProvider(CategoriesDifferProvider::class.java).singletonInScope()
        bind(CategoryItemCallback::class.java).toProvider(CategoryItemCallbackProvider::class.java).singletonInScope()
    }
}