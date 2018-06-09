package app.suhocki.mybooks.ui.filter

import android.support.v7.util.EndActionDiffUtil
import app.suhocki.mybooks.domain.model.Search
import app.suhocki.mybooks.domain.model.filter.*

internal class FilterDiffCallback : EndActionDiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return if (oldItem is FilterCategory && newItem is FilterCategory) oldItem.title == newItem.title
        else if (oldItem is FilterAuthor && newItem is FilterAuthor) oldItem.authorName == newItem.authorName
        else if (oldItem is FilterPublisher && newItem is FilterPublisher) oldItem.publisherName == newItem.publisherName
        else if (oldItem is FilterStatus && newItem is FilterStatus) oldItem.status == newItem.status
        else if (oldItem is FilterYear && newItem is FilterYear) oldItem.year == newItem.year
        else if (oldItem is FilterPrice && newItem is FilterPrice) true
        else if (oldItem is SortName && newItem is SortName) oldItem.sortName == newItem.sortName
        else if (oldItem is SortPrice && newItem is SortPrice) oldItem.sortName == newItem.sortName
        else if (oldItem is Search && newItem is Search) oldItem.hintRes == newItem.hintRes
        else false
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return if (oldItem is FilterCategory && newItem is FilterCategory) false
        else if (oldItem is FilterAuthor && newItem is FilterAuthor) oldItem.isChecked == newItem.isChecked
        else if (oldItem is FilterPublisher && newItem is FilterPublisher) oldItem.isChecked == newItem.isChecked
        else if (oldItem is FilterStatus && newItem is FilterStatus) oldItem.isChecked == newItem.isChecked
        else if (oldItem is FilterYear && newItem is FilterYear) oldItem.isChecked == newItem.isChecked
        else if (oldItem is FilterPrice && newItem is FilterPrice) true
        else if (oldItem is Search && newItem is Search) true
        else if (oldItem is SortName && newItem is SortName) oldItem.isChecked == newItem.isChecked
        else if (oldItem is SortPrice && newItem is SortPrice) oldItem.isChecked == newItem.isChecked
        else false
    }
}