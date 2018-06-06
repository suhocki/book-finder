package app.suhocki.mybooks.ui.search

import android.support.v7.util.DiffUtil
import app.suhocki.mybooks.domain.model.filter.FilterAuthor
import app.suhocki.mybooks.domain.model.filter.FilterPublisher

internal class SearchDiffCallback : DiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return if (oldItem is FilterAuthor && newItem is FilterAuthor) oldItem.authorName == newItem.authorName
        else if (oldItem is FilterPublisher && newItem is FilterPublisher) oldItem.publisherName == newItem.publisherName
        else false
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return if (oldItem is FilterAuthor && newItem is FilterAuthor) oldItem.isChecked == newItem.isChecked
        else if (oldItem is FilterPublisher && newItem is FilterPublisher) oldItem.isChecked == newItem.isChecked
        else false
    }
}