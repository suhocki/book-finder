package app.suhocki.mybooks.ui.filter

import android.support.v7.recyclerview.extensions.AsyncListDiffer
import app.suhocki.mybooks.ui.base.adapter.delegate.filter.*
import app.suhocki.mybooks.ui.base.listener.filter.*
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter


class FilterAdapter(
    onFilterCategoryClickListener: OnFilterCategoryClickListener,
    onFilterAuthorClickListener: OnFilterAuthorClickListener,
    onFilterPublisherClickListener: OnFilterPublisherClickListener,
    onFilterStatusClickListener: OnFilterStatusClickListener,
    onFilterYearClickListener: OnFilterYearClickListener
) : ListDelegationAdapter<MutableList<Any>>() {

    private val differ by lazy { AsyncListDiffer(this, FilterDiffCallback()) }

    init {
        delegatesManager
            .addDelegate(FilterCategoryAdapterDelegate(onFilterCategoryClickListener))
            .addDelegate(FilterAuthorAdapterDelegate(onFilterAuthorClickListener))
            .addDelegate(FilterPublisherAdapterDelegate(onFilterPublisherClickListener))
            .addDelegate(FilterYearAdapterDelegate(onFilterYearClickListener))
            .addDelegate(FilterStatusAdapterDelegate(onFilterStatusClickListener))
    }

    override fun getItemCount(): Int =
        differ.currentList.size

    fun submitList(list: List<Any>) =
        mutableListOf<Any>().apply {
            addAll(list)
            items = this
            differ.submitList(this)
        }
}