package app.suhocki.mybooks.ui.filter

import android.support.v7.recyclerview.extensions.AsyncDifferConfig
import android.support.v7.recyclerview.extensions.AsyncListDiffer
import app.suhocki.mybooks.ui.base.EndActionAdapterListUpdateCallback
import app.suhocki.mybooks.ui.base.delegate.FilterAuthorAdapterDelegate
import app.suhocki.mybooks.ui.base.delegate.FilterPublisherAdapterDelegate
import app.suhocki.mybooks.ui.base.delegate.SearchAdapterDelegate
import app.suhocki.mybooks.ui.base.listener.OnSearchClickListener
import app.suhocki.mybooks.ui.filter.delegate.*
import app.suhocki.mybooks.ui.filter.listener.OnFilterCategoryClickListener
import app.suhocki.mybooks.ui.filter.listener.OnSortPriceClickListener
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter


class FilterAdapter(
    onSortPriceClickListener: OnSortPriceClickListener,
    onFilterCategoryClickListener: OnFilterCategoryClickListener,
    onSearchClickListener: OnSearchClickListener
) : ListDelegationAdapter<MutableList<Any>>() {

    private val listUpdateCallback by lazy { EndActionAdapterListUpdateCallback(this, null) }

    private val diffConfig by lazy { AsyncDifferConfig.Builder<Any>(FilterDiffCallback()).build() }

    private val differ by lazy { AsyncListDiffer(listUpdateCallback, diffConfig) }

    init {
        delegatesManager
            .addDelegate(FilterCategoryAdapterDelegate(onFilterCategoryClickListener))
            .addDelegate(FilterAuthorAdapterDelegate())
            .addDelegate(FilterPublisherAdapterDelegate())
            .addDelegate(FilterYearAdapterDelegate())
            .addDelegate(FilterStatusAdapterDelegate())
            .addDelegate(FilterPriceAdapterDelegate())
            .addDelegate(SearchAdapterDelegate(onSearchClickListener))
            .addDelegate(SortNameAdapterDelegate())
            .addDelegate(SortPriceAdapterDelegate(onSortPriceClickListener))
    }

    override fun getItemCount(): Int =
        differ.currentList.size

    fun submitList(list: List<Any>, onAnimationEnd: () -> Unit) {
        listUpdateCallback.endAction = onAnimationEnd
        mutableListOf<Any>().apply {
            addAll(list)
            items = this
            differ.submitList(this)
        }
    }
}