package app.suhocki.mybooks.di.module

import app.suhocki.mybooks.di.HeaderCatalogItem
import app.suhocki.mybooks.di.SearchCatalogItem
import app.suhocki.mybooks.di.provider.presentation.HeaderCatalogItemProvider
import app.suhocki.mybooks.di.provider.presentation.SearchCatalogItemProvider
import app.suhocki.mybooks.domain.model.CatalogItem
import toothpick.config.Module

class CatalogActivityModule : Module() {
    init {
        bind(CatalogItem::class.java).withName(SearchCatalogItem::class.java).toProvider(SearchCatalogItemProvider::class.java)
        bind(CatalogItem::class.java).withName(HeaderCatalogItem::class.java).toProvider(HeaderCatalogItemProvider::class.java)
    }
}