package app.suhocki.mybooks.ui.catalog

import android.support.v7.util.EndActionDiffUtil
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.model.Header
import app.suhocki.mybooks.domain.model.SearchResult

internal class CatalogDiffCallback : EndActionDiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return if (oldItem is Category && newItem is Category) oldItem.name == newItem.name
        else if (oldItem is SearchResult && newItem is SearchResult) oldItem.foundBy == newItem.foundBy
        else oldItem::class.java == newItem::class.java
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return if (oldItem is Category && newItem is Category) oldItem.name == newItem.name
        else if (oldItem is Header && newItem is Header) false
        else true
    }
}