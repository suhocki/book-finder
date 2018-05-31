package app.suhocki.mybooks.ui.catalog

import android.support.v7.recyclerview.extensions.AsyncDifferConfig
import android.support.v7.recyclerview.extensions.AsyncListDiffer
import app.suhocki.mybooks.domain.model.Search
import app.suhocki.mybooks.ui.base.adapter.AdapterListUpdateCallback
import app.suhocki.mybooks.ui.base.adapter.delegate.*
import app.suhocki.mybooks.ui.base.listener.OnCategoryClickListener
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter

class CatalogAdapter(
    onCategoryClickListener: OnCategoryClickListener,
    search: Search
) : ListDelegationAdapter<MutableList<Any>>() {

    private val listUpdateCallback by lazy { AdapterListUpdateCallback(this, null) }

    private val diffConfig by lazy { AsyncDifferConfig.Builder<Any>(CatalogDiffCallback()).build() }

    private val differ by lazy { AsyncListDiffer(listUpdateCallback, diffConfig) }

    init {
        delegatesManager.addDelegate(CategoryAdapterDelegate(onCategoryClickListener))
        delegatesManager.addDelegate(BannerAdapterDelegate())
        delegatesManager.addDelegate(HeaderAdapterDelegate())
        delegatesManager.addDelegate(SearchAdapterDelegate(search))
        delegatesManager.addDelegate(HintAdapterDelegate())
    }

    override fun getItemCount(): Int =
        differ.currentList.size

    fun submitList(list: List<Any>, onAnimationEndAction: () -> Unit) {
        listUpdateCallback.endAction = onAnimationEndAction
        mutableListOf<Any>().apply {
            addAll(list)
            items = this
            differ.submitList(this)
        }
    }
}