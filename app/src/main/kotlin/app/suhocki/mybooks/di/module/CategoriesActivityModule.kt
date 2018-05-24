package app.suhocki.mybooks.di.module

import android.support.v7.widget.LinearLayoutManager
import app.suhocki.mybooks.di.CategoriesStartFlag
import app.suhocki.mybooks.di.HeaderCatalogItem
import app.suhocki.mybooks.di.PrimitiveWrapper
import app.suhocki.mybooks.di.SearchCatalogItem
import app.suhocki.mybooks.di.provider.presentation.*
import app.suhocki.mybooks.domain.model.CatalogItem
import app.suhocki.mybooks.presentation.catalog.adapter.CatalogAdapter
import app.suhocki.mybooks.presentation.catalog.adapter.CatalogDiffer
import app.suhocki.mybooks.presentation.catalog.adapter.CatalogItemCallback
import toothpick.config.Module

class CategoriesActivityModule(startFromNotification: Boolean) : Module() {
    init {
        bind(PrimitiveWrapper::class.java).withName(CategoriesStartFlag::class.java).toInstance(PrimitiveWrapper(startFromNotification))
        bind(CatalogAdapter::class.java).singletonInScope()
        bind(LinearLayoutManager::class.java).toProvider(LinearLayoutManagerProvider::class.java).singletonInScope()
        bind(CatalogDiffer::class.java).toProvider(CategoriesDifferProvider::class.java)
        bind(CatalogItemCallback::class.java).toProvider(CategoryItemCallbackProvider::class.java)
        bind(CatalogItem::class.java).withName(SearchCatalogItem::class.java).toProvider(SearchCatalogItemProvider::class.java)
        bind(CatalogItem::class.java).withName(HeaderCatalogItem::class.java).toProvider(HeaderCatalogItemProvider::class.java)
    }
}