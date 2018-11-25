package app.suhocki.mybooks.ui.changelog

import android.support.v7.recyclerview.extensions.EndActionAsyncDifferConfig
import android.support.v7.recyclerview.extensions.EndActionAsyncListDiffer
import app.suhocki.mybooks.ui.admin.delegate.HeaderAdapterDelegate
import app.suhocki.mybooks.ui.base.EndActionAdapterListUpdateCallback
import app.suhocki.mybooks.ui.books.BooksDiffCallback
import app.suhocki.mybooks.ui.changelog.delegate.ChangelogAdapterDelegate
import app.suhocki.mybooks.ui.changelog.listener.OnDownloadFileClickListener
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter

class ChangelogAdapter(
    onDownloadFileClickListener: OnDownloadFileClickListener
) :  ListDelegationAdapter<MutableList<Any>>() {

    private val listUpdateCallback by lazy { EndActionAdapterListUpdateCallback(this, null) }

    private val diffConfig by lazy { EndActionAsyncDifferConfig.Builder<Any>(BooksDiffCallback()).build() }

    private val differ by lazy { EndActionAsyncListDiffer(listUpdateCallback, diffConfig) }

    init {
        delegatesManager.addDelegate(ChangelogAdapterDelegate(onDownloadFileClickListener))
        delegatesManager.addDelegate(HeaderAdapterDelegate())
    }

    override fun getItemCount(): Int =
        differ.currentList.size

    fun submitList(list: List<Any>) {
        mutableListOf<Any>().apply {
            addAll(list)
            items = this
            differ.submitList(this)
        }
    }
}