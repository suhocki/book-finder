package app.suhocki.mybooks.ui.search

import android.support.v7.recyclerview.extensions.EndActionAsyncDifferConfig
import android.support.v7.recyclerview.extensions.EndActionAsyncListDiffer
import app.suhocki.mybooks.ui.base.EndActionAdapterListUpdateCallback
import app.suhocki.mybooks.ui.base.delegate.FilterAuthorAdapterDelegate
import app.suhocki.mybooks.ui.base.delegate.FilterPublisherAdapterDelegate
import app.suhocki.mybooks.ui.base.listener.OnFilterAuthorClickListener
import app.suhocki.mybooks.ui.base.listener.OnFilterPublisherClickListener
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter


class SearchAdapter(
    onFilterAuthorClickListener: OnFilterAuthorClickListener,
    onFilterPublisherClickListener: OnFilterPublisherClickListener
) : ListDelegationAdapter<MutableList<Any>>() {

    private val listUpdateCallback by lazy { EndActionAdapterListUpdateCallback(this, null) }

    private val diffConfig by lazy { EndActionAsyncDifferConfig.Builder<Any>(SearchDiffCallback()).build() }

    private val differ by lazy { EndActionAsyncListDiffer(listUpdateCallback, diffConfig) }

    init {
        delegatesManager
            .addDelegate(
                FilterAuthorAdapterDelegate(
                    onFilterAuthorClickListener
                )
            )
            .addDelegate(
                FilterPublisherAdapterDelegate(
                    onFilterPublisherClickListener
                )
            )
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