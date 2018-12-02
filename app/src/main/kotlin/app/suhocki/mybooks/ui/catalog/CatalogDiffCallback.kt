package app.suhocki.mybooks.ui.catalog

import android.support.v7.util.EndActionDiffUtil
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.model.Header
import app.suhocki.mybooks.domain.model.SearchResult
import app.suhocki.mybooks.ui.base.entity.UiItem

internal class CatalogDiffCallback : EndActionDiffUtil.ItemCallback<UiItem>() {

    override fun areItemsTheSame(oldItem: UiItem, newItem: UiItem) = when {
        oldItem is Category && newItem is Category -> oldItem.id == newItem.id

        oldItem is SearchResult && newItem is SearchResult ->
            oldItem.book.id == newItem.book.id

        else -> oldItem::class.java == newItem::class.java
    }

    override fun areContentsTheSame(oldItem: UiItem, newItem: UiItem) = when {
        oldItem is Category && newItem is Category ->
            oldItem.name == newItem.name && oldItem.booksCount == newItem.booksCount

        oldItem is Header && newItem is Header ->
            oldItem.title == newItem.title

        else -> true
    }

    override fun getChangePayload(oldItem: UiItem?, newItem: UiItem?): Any {
        return Any()
    }
}