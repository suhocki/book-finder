package app.suhocki.mybooks.di.provider.presentation

import app.suhocki.mybooks.domain.model.CatalogItem
import app.suhocki.mybooks.presentation.catalog.adapter.CatalogItemType
import javax.inject.Inject
import javax.inject.Provider

class SearchCatalogItemProvider @Inject constructor() :
    Provider<CatalogItem> {

    override fun get(): CatalogItem = object : CatalogItem {
        override val type: CatalogItemType
            get() = CatalogItemType.SEARCH
    }
}