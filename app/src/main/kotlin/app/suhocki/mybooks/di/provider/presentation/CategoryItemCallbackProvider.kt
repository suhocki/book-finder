package app.suhocki.mybooks.di.provider.presentation

import android.support.v7.util.DiffUtil
import app.suhocki.mybooks.domain.model.CatalogItem
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.presentation.catalog.adapter.CatalogItemCallback
import javax.inject.Inject
import javax.inject.Provider

class CategoryItemCallbackProvider @Inject constructor() :
    Provider<CatalogItemCallback> {

    override fun get(): CatalogItemCallback = CatalogItemCallback().apply {
        set(object : DiffUtil.ItemCallback<CatalogItem>() {
            override fun areItemsTheSame(oldItem: CatalogItem, newItem: CatalogItem): Boolean {
                return if (oldItem is Category && newItem is Category) oldItem.name == newItem.name
                else oldItem.type == newItem.type
            }

            override fun areContentsTheSame(oldItem: CatalogItem, newItem: CatalogItem): Boolean {
                return if (oldItem is Category && newItem is Category) oldItem.name == newItem.name
                else oldItem.type == newItem.type
            }
        })
    }
}