package app.suhocki.mybooks.ui.catalog

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.model.Header
import app.suhocki.mybooks.ui.base.delegate.ProgressAdapterDelegate
import app.suhocki.mybooks.ui.catalog.delegate.BannersHolderAdapterDelegate
import app.suhocki.mybooks.ui.catalog.delegate.CategoryAdapterDelegate
import app.suhocki.mybooks.ui.catalog.entity.BannersHolder
import com.hannesdorfmann.adapterdelegates3.AsyncListDifferDelegationAdapter

class CatalogAdapter(
    diffCallback: CatalogDiffCallback,
    categoryClickListener: (Category) -> Unit,
    loadNextBannersPage: () -> Unit,
    private val loadNextCategoriesPage: () -> Unit
) : AsyncListDifferDelegationAdapter<Any>(diffCallback) {

    init {
        delegatesManager
            .addDelegate(CategoryAdapterDelegate(categoryClickListener))
            .addDelegate(ProgressAdapterDelegate())
            .addDelegate(BannersHolderAdapterDelegate(loadNextBannersPage))
    }

    fun setData(list: List<Any>) {
        items = list.toList()
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any?>
    ) {
        super.onBindViewHolder(holder, position, payloads)

        if (position == items.lastIndex) loadNextCategoriesPage()
    }

    class CatalogDiffCallback : DiffUtil.ItemCallback<Any>() {

        override fun areItemsTheSame(oldItem: Any, newItem: Any) = when {
            oldItem is Category && newItem is Category -> oldItem.id == newItem.id

            else -> oldItem::class.java == newItem::class.java
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any) = when {
            oldItem is Category && newItem is Category ->
                oldItem.name == newItem.name && oldItem.booksCount == newItem.booksCount

            oldItem is BannersHolder && newItem is BannersHolder ->
                false

            oldItem is Header && newItem is Header ->
                oldItem.title == newItem.title

            else -> true
        }

        override fun getChangePayload(oldItem: Any, newItem: Any): Any? {
            return if (oldItem is BannersHolder && newItem is BannersHolder) Any()
            else super.getChangePayload(oldItem, newItem)
        }
    }
}