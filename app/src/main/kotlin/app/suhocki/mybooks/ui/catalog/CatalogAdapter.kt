package app.suhocki.mybooks.ui.catalog

import android.support.v7.recyclerview.extensions.AsyncDifferConfig
import android.support.v7.recyclerview.extensions.AsyncListDiffer
import app.suhocki.mybooks.domain.model.Search
import app.suhocki.mybooks.ui.base.adapter.AdapterListUpdateCallback
import app.suhocki.mybooks.ui.base.adapter.delegate.catalog.*
import app.suhocki.mybooks.ui.base.listener.OnBookClickListener
import app.suhocki.mybooks.ui.base.listener.catalog.OnCategoryClickListener
import app.suhocki.mybooks.ui.base.listener.catalog.OnSearchClickListener
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter

class CatalogAdapter(
    onCategoryClickListener: OnCategoryClickListener,
    onSearchClickListener: OnSearchClickListener,
    onBookClickListener: OnBookClickListener,
    search: Search
) : ListDelegationAdapter<MutableList<Any>>() {

    private val listUpdateCallback by lazy { AdapterListUpdateCallback(this, null) }

    private val diffConfig by lazy { AsyncDifferConfig.Builder<Any>(CatalogDiffCallback()).build() }

    private val differ by lazy { AsyncListDiffer(listUpdateCallback, diffConfig) }

    init {
        delegatesManager
            .addDelegate(
                CategoryAdapterDelegate(
                    onCategoryClickListener
                )
            )
            .addDelegate(BannerAdapterDelegate())
            .addDelegate(HeaderAdapterDelegate())
            .addDelegate(
                SearchAdapterDelegate(
                    search,
                    onSearchClickListener
                )
            )
            .addDelegate(
                SearchResultAdapterDelegate(
                    onBookClickListener
                )
            )
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