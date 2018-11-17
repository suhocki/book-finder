package app.suhocki.mybooks.ui.catalog

import android.support.v7.util.EndActionDiffUtil
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.model.Header
import app.suhocki.mybooks.domain.model.SearchResult

internal class CatalogDiffCallback : EndActionDiffUtil.ItemCallback<Any>() {

    override fun areItemsTheSame(oldItem: Any, newItem: Any) = when {
        oldItem is Category && newItem is Category -> oldItem.id == newItem.id

        oldItem is SearchResult && newItem is SearchResult ->
            oldItem.book.productCode == newItem.book.productCode

        else -> oldItem::class.java == newItem::class.java
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any) = when {
        oldItem is Category && newItem is Category -> oldItem == newItem

        oldItem is Header && newItem is Header ->
            oldItem.title == newItem.title

        else -> true
    }

    override fun getChangePayload(oldItem: Any?, newItem: Any?): Any {
        return Any()
    }
}