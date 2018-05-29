package app.suhocki.mybooks.ui.catalog.adapter

import android.support.v7.util.DiffUtil
import app.suhocki.mybooks.domain.model.CatalogItem
import app.suhocki.mybooks.domain.model.Category

internal class CatalogDiffCallback : DiffUtil.ItemCallback<CatalogItem>() {
    override fun areItemsTheSame(oldItem: CatalogItem, newItem: CatalogItem): Boolean {
        return if (oldItem is Category && newItem is Category) oldItem.name == newItem.name
        else oldItem.type == newItem.type
    }

    override fun areContentsTheSame(oldItem: CatalogItem, newItem: CatalogItem): Boolean {
        return if (oldItem is Category && newItem is Category) oldItem.name == newItem.name
        else oldItem.type == newItem.type
    }
}