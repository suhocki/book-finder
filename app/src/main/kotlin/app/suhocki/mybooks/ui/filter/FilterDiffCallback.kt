package app.suhocki.mybooks.ui.filter

import android.support.v7.util.DiffUtil
import app.suhocki.mybooks.domain.model.filter.*

internal class FilterDiffCallback : DiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return if (oldItem is FilterCategory && newItem is FilterCategory) oldItem.title == newItem.title
        else if (oldItem is FilterAuthor && newItem is FilterAuthor) oldItem.authorName == newItem.authorName
        else if (oldItem is FilterPublisher && newItem is FilterPublisher) oldItem.publisherName == newItem.publisherName
        else if (oldItem is FilterStatus && newItem is FilterStatus) oldItem.status == newItem.status
        else if (oldItem is FilterYear && newItem is FilterYear) oldItem.year == newItem.year
        else false
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return if (oldItem is FilterCategory && newItem is FilterCategory) oldItem.isExpanded == newItem.isExpanded
        else if (oldItem is FilterAuthor && newItem is FilterAuthor) oldItem.isChecked == newItem.isChecked
        else if (oldItem is FilterPublisher && newItem is FilterPublisher) oldItem.isChecked == newItem.isChecked
        else if (oldItem is FilterStatus && newItem is FilterStatus) oldItem.isChecked == newItem.isChecked
        else if (oldItem is FilterYear && newItem is FilterYear) oldItem.isChecked == newItem.isChecked
        else false
    }
}