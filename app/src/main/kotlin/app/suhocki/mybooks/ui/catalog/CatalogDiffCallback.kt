package app.suhocki.mybooks.ui.catalog

import android.support.v7.util.DiffUtil
import app.suhocki.mybooks.domain.model.Category

internal class CatalogDiffCallback : DiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return if (oldItem is Category && newItem is Category) oldItem.name == newItem.name
        else oldItem::class.java == newItem::class.java
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return if (oldItem is Category && newItem is Category) oldItem.name == newItem.name
        else oldItem::class.java == newItem::class.java
    }
}