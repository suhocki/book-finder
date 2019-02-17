package app.suhocki.mybooks.ui.changelog

import android.support.v7.util.DiffUtil
import app.suhocki.mybooks.domain.model.Changelog
import app.suhocki.mybooks.ui.admin.delegate.HeaderAdapterDelegate
import app.suhocki.mybooks.ui.changelog.delegate.ChangelogAdapterDelegate
import com.hannesdorfmann.adapterdelegates3.AsyncListDifferDelegationAdapter

class ChangelogAdapter(
    diffCallback: ChangelogDiffCallback,
    onDownloadFileClick: (String) -> Unit
) : AsyncListDifferDelegationAdapter<Any>(diffCallback) {

    init {
        delegatesManager.addDelegate(ChangelogAdapterDelegate(onDownloadFileClick))
        delegatesManager.addDelegate(HeaderAdapterDelegate())
    }

    fun setData(list: List<Any>) {
        items = list.toList()
    }

    class ChangelogDiffCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any) = when {
            oldItem is Changelog && newItem is Changelog -> oldItem.version == newItem.version
            else -> oldItem::class.java == newItem::class.java
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any) = when {
            oldItem is Changelog && newItem is Changelog ->
                oldItem.version == newItem.version &&
                        oldItem.changes.contentDeepEquals(newItem.changes) &&
                        oldItem.date == newItem.date &&
                        oldItem.link == newItem.link

            else -> true
        }
    }

}