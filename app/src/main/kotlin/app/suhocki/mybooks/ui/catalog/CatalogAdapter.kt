package app.suhocki.mybooks.ui.catalog

import android.support.v7.recyclerview.extensions.EndActionAsyncDifferConfig
import android.support.v7.recyclerview.extensions.EndActionAsyncListDiffer
import app.suhocki.mybooks.ui.base.EndActionAdapterListUpdateCallback
import app.suhocki.mybooks.ui.base.delegate.SearchAdapterDelegate
import app.suhocki.mybooks.ui.base.listener.OnBookClickListener
import app.suhocki.mybooks.ui.base.listener.OnSearchClickListener
import app.suhocki.mybooks.ui.catalog.delegate.BannerAdapterDelegate
import app.suhocki.mybooks.ui.catalog.delegate.CategoryAdapterDelegate
import app.suhocki.mybooks.ui.catalog.delegate.HeaderAdapterDelegate
import app.suhocki.mybooks.ui.catalog.delegate.SearchResultBookAdapterDelegate
import app.suhocki.mybooks.ui.catalog.listener.OnCategoryClickListener
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter

class CatalogAdapter(
    onCategoryClickListener: OnCategoryClickListener,
    onBookClickListener: OnBookClickListener,
    onSearchClickListener: OnSearchClickListener
) : ListDelegationAdapter<MutableList<Any>>() {

    private val listUpdateCallback by lazy { EndActionAdapterListUpdateCallback(this, null) }

    private val diffConfig by lazy { EndActionAsyncDifferConfig.Builder<Any>(CatalogDiffCallback()).build() }

    private val differ by lazy { EndActionAsyncListDiffer(listUpdateCallback, diffConfig) }

    init {
        delegatesManager
            .addDelegate(CategoryAdapterDelegate(onCategoryClickListener))
            .addDelegate(BannerAdapterDelegate())
            .addDelegate(HeaderAdapterDelegate())
            .addDelegate(
                SearchAdapterDelegate(
                    onSearchClickListener
                )
            )
            .addDelegate(SearchResultBookAdapterDelegate(onBookClickListener))
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