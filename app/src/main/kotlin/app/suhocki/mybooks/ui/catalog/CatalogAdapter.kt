package app.suhocki.mybooks.ui.catalog

import android.support.v7.recyclerview.extensions.EndActionAsyncDifferConfig
import android.support.v7.recyclerview.extensions.EndActionAsyncListDiffer
import app.suhocki.mybooks.ui.base.EndActionAdapterListUpdateCallback
import app.suhocki.mybooks.ui.base.delegate.SearchAdapterDelegate
import app.suhocki.mybooks.ui.base.listener.OnBookClickListener
import app.suhocki.mybooks.ui.base.listener.OnSearchClickListener
import app.suhocki.mybooks.ui.base.listener.OnSearchListener
import app.suhocki.mybooks.ui.catalog.delegate.*
import app.suhocki.mybooks.ui.catalog.listener.OnCategoryClickListener
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter

class CatalogAdapter(
    onCategoryClickListener: OnCategoryClickListener,
    onBookClickListener: OnBookClickListener,
    onSearchClickListener: OnSearchClickListener,
    onSearchListener: OnSearchListener
) : ListDelegationAdapter<MutableList<Any>>() {

    private val listUpdateCallback by lazy { EndActionAdapterListUpdateCallback(this, null) }

    private val diffConfig by lazy {
        EndActionAsyncDifferConfig.Builder<Any>(CatalogDiffCallback()).build()
    }

    private val differ by lazy { EndActionAsyncListDiffer(listUpdateCallback, diffConfig) }

    init {
        delegatesManager
            .addDelegate(BannerAdAdapterDelegate())
            .addDelegate(CategoryAdapterDelegate(onCategoryClickListener))
            .addDelegate(BannerAdapterDelegate())
            .addDelegate(HeaderAdapterDelegate())
            .addDelegate(SearchAdapterDelegate(onSearchClickListener, onSearchListener))
            .addDelegate(SearchResultBookAdapterDelegate(onBookClickListener))
    }

    override fun getItemCount(): Int =
        differ.currentList.size

    fun submitList(list: List<Any>, onAnimationEnd: () -> Unit) {
        if (items != null && items.size == list.size && items.containsAll(list)) {
            return
        }
        listUpdateCallback.endAction = onAnimationEnd
        mutableListOf<Any>().apply {
            addAll(list)
            items = this
            differ.submitList(this)
        }
    }
}