package app.suhocki.mybooks.ui.filter

import android.support.v7.recyclerview.extensions.AsyncDifferConfig
import android.support.v7.recyclerview.extensions.AsyncListDiffer
import app.suhocki.mybooks.ui.base.EndActionAdapterListUpdateCallback
import app.suhocki.mybooks.ui.base.search.OnSearchClickListener
import app.suhocki.mybooks.ui.base.search.SearchAdapterDelegate
import app.suhocki.mybooks.ui.filter.delegate.*
import app.suhocki.mybooks.ui.filter.listener.*
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter


class FilterAdapter(
    onSortPriceClickListener: OnSortPriceClickListener,
    onSortNameClickListener: OnSortNameClickListener,
    onFilterCategoryClickListener: OnFilterCategoryClickListener,
    onFilterAuthorClickListener: OnFilterAuthorClickListener,
    onFilterPublisherClickListener: OnFilterPublisherClickListener,
    onFilterStatusClickListener: OnFilterStatusClickListener,
    onFilterYearClickListener: OnFilterYearClickListener,
    onSearchClickListener: OnSearchClickListener
) : ListDelegationAdapter<MutableList<Any>>() {

    private val listUpdateCallback by lazy { EndActionAdapterListUpdateCallback(this, null) }

    private val diffConfig by lazy { AsyncDifferConfig.Builder<Any>(FilterDiffCallback()).build() }

    private val differ by lazy { AsyncListDiffer(listUpdateCallback, diffConfig) }

    init {
        delegatesManager
            .addDelegate(FilterCategoryAdapterDelegate(onFilterCategoryClickListener))
            .addDelegate(FilterAuthorAdapterDelegate(onFilterAuthorClickListener))
            .addDelegate(FilterPublisherAdapterDelegate(onFilterPublisherClickListener))
            .addDelegate(FilterYearAdapterDelegate(onFilterYearClickListener))
            .addDelegate(FilterStatusAdapterDelegate(onFilterStatusClickListener))
            .addDelegate(FilterPriceAdapterDelegate())
            .addDelegate(SearchAdapterDelegate(onSearchClickListener))
            .addDelegate(SortNameAdapterDelegate(onSortNameClickListener))
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