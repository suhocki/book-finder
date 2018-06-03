package app.suhocki.mybooks.ui.filter

import android.support.v7.recyclerview.extensions.AsyncListDiffer
import app.suhocki.mybooks.ui.base.adapter.delegate.FilterCategoryAdapterDelegate
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter


class FilterAdapter : ListDelegationAdapter<MutableList<Any>>() {

    private val differ by lazy { AsyncListDiffer(this, FilterDiffCallback()) }

    init {
        delegatesManager.addDelegate(
            FilterCategoryAdapterDelegate()
        )
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