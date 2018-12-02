package app.suhocki.mybooks.ui.catalog

import android.support.v7.recyclerview.extensions.EndActionAsyncDifferConfig
import android.support.v7.recyclerview.extensions.EndActionAsyncListDiffer
import android.support.v7.widget.RecyclerView
import app.suhocki.mybooks.ui.base.EndActionAdapterListUpdateCallback
import app.suhocki.mybooks.ui.base.delegate.ProgressAdapterDelegate
import app.suhocki.mybooks.ui.base.entity.UiItem
import app.suhocki.mybooks.ui.base.listener.OnBookClickListener
import app.suhocki.mybooks.ui.base.listener.OnSearchClickListener
import app.suhocki.mybooks.ui.base.listener.OnSearchListener
import app.suhocki.mybooks.ui.catalog.delegate.*
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter

class CatalogAdapter(
    private val loadNextPage: () -> Unit,
    onCategoryClick: (String) -> Unit,
    onBookClickListener: OnBookClickListener,
    onSearchClickListener: OnSearchClickListener,
    onSearchListener: OnSearchListener
) : ListDelegationAdapter<MutableList<UiItem>>() {

    private val listUpdateCallback by lazy { EndActionAdapterListUpdateCallback(this, null) }

    private val diffConfig by lazy {
        EndActionAsyncDifferConfig.Builder<UiItem>(CatalogDiffCallback()).build()
    }

    private val differ by lazy { EndActionAsyncListDiffer(listUpdateCallback, diffConfig) }

    init {
        delegatesManager
            .addDelegate(BannerAdAdapterDelegate())
            .addDelegate(CategoryAdapterDelegate(onCategoryClick))
            .addDelegate(BannerAdapterDelegate())
            .addDelegate(ProgressAdapterDelegate())
            .addDelegate(HeaderAdapterDelegate())
//            .addDelegate(SearchAdapterDelegate(onSearchClickListener, onSearchListener))
            .addDelegate(SearchResultBookAdapterDelegate(onBookClickListener))
    }

    fun submitList(list: List<UiItem>, onAnimationEnd: (() -> Unit)? = null) {
        items = list.toMutableList()
        differ.submitList(list.toList())
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any?>
    ) {
        super.onBindViewHolder(holder, position, payloads)

        if (items[position].isNextPageTrigger) {
            items[position].isNextPageTrigger = false
            loadNextPage.invoke()
        }
    }
}