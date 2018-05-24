package app.suhocki.mybooks.di.provider.presentation

import android.support.v7.recyclerview.extensions.AsyncListDiffer
import app.suhocki.mybooks.domain.model.CatalogItem
import app.suhocki.mybooks.presentation.catalog.adapter.CatalogAdapter
import app.suhocki.mybooks.presentation.catalog.adapter.CatalogDiffer
import app.suhocki.mybooks.presentation.catalog.adapter.CatalogItemCallback
import javax.inject.Inject
import javax.inject.Provider

class CategoriesDifferProvider @Inject constructor(
    private val adapter: CatalogAdapter,
    private val itemCallback: CatalogItemCallback
): Provider<CatalogDiffer> {

    override fun get(): CatalogDiffer = CatalogDiffer().apply {
        set(AsyncListDiffer<CatalogItem>(adapter, itemCallback.get()))
    }
}