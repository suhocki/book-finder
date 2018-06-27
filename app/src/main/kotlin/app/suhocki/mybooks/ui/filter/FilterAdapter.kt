package app.suhocki.mybooks.ui.filter

import android.support.v7.recyclerview.extensions.EndActionAsyncDifferConfig
import android.support.v7.recyclerview.extensions.EndActionAsyncListDiffer
import app.suhocki.mybooks.ui.base.EndActionAdapterListUpdateCallback
import app.suhocki.mybooks.ui.base.delegate.FilterAuthorAdapterDelegate
import app.suhocki.mybooks.ui.base.delegate.FilterPublisherAdapterDelegate
import app.suhocki.mybooks.ui.base.delegate.SearchAdapterDelegate
import app.suhocki.mybooks.ui.base.listener.OnFilterAuthorClickListener
import app.suhocki.mybooks.ui.base.listener.OnFilterPublisherClickListener
import app.suhocki.mybooks.ui.base.listener.OnSearchClickListener
import app.suhocki.mybooks.ui.filter.delegate.*
import app.suhocki.mybooks.ui.filter.listener.*
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter


class FilterAdapter(
    onSortPriceToggleListener: SortPriceListener,
    onSortNameToggleListener: SortNameListener,
    onFilterCategoryClickListener: OnFilterCategoryClickListener,
    onSearchClickListener: OnSearchClickListener,
    onFilterAuthorClickListener: OnFilterAuthorClickListener,
    yearClickListener: OnFilterYearClickListener,
    onFilterPriceChangeListener: OnFilterPriceChangeListener,
    onFilterStatusClickListener: OnFilterStatusClickListener,
    onFilterPublisherClickListener: OnFilterPublisherClickListener
) : ListDelegationAdapter<MutableList<Any>>() {

    private val listUpdateCallback by lazy { EndActionAdapterListUpdateCallback(this, null) }

    private val diffConfig by lazy { EndActionAsyncDifferConfig.Builder<Any>(FilterDiffCallback()).build() }

    private val differ by lazy { EndActionAsyncListDiffer(listUpdateCallback, diffConfig) }

    init {
        delegatesManager
            .addDelegate(FilterCategoryAdapterDelegate(onFilterCategoryClickListener))
            .addDelegate(FilterAuthorAdapterDelegate(onFilterAuthorClickListener))
            .addDelegate(FilterPublisherAdapterDelegate(onFilterPublisherClickListener))
            .addDelegate(FilterYearAdapterDelegate(yearClickListener))
            .addDelegate(FilterStatusAdapterDelegate(onFilterStatusClickListener))
            .addDelegate(FilterPriceAdapterDelegate(onFilterPriceChangeListener))
            .addDelegate(SearchAdapterDelegate(onSearchClickListener))
            .addDelegate(SortNameAdapterDelegate(onSortNameToggleListener))
            .addDelegate(SortPriceAdapterDelegate(onSortPriceToggleListener))
            .addDelegate(EmptyCategoryAdapterDelegate())
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