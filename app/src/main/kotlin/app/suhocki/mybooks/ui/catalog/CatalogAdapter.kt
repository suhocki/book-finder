package app.suhocki.mybooks.ui.catalog

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.model.Header
import app.suhocki.mybooks.ui.base.delegate.ProgressAdapterDelegate
import app.suhocki.mybooks.ui.base.entity.Progress
import app.suhocki.mybooks.ui.catalog.delegate.CategoryAdapterDelegate
import com.hannesdorfmann.adapterdelegates3.AsyncListDifferDelegationAdapter

class CatalogAdapter(
    diffCallback: CatalogDiffCallback,
    categoryClickListener: (Category) -> Unit,
    private val nextPageListener: () -> Unit
) : AsyncListDifferDelegationAdapter<Any>(diffCallback) {

    init {
        delegatesManager
            .addDelegate(CategoryAdapterDelegate(categoryClickListener))
            .addDelegate(ProgressAdapterDelegate())
    }

    fun setData(list: List<Any>) {
        items = list.toList()
    }

    fun showProgress(isVisible: Boolean) {
        val newData = items.toMutableList()

        if (isVisible && items.lastOrNull() !is Progress) {
            newData.add(Progress())
            items = newData
        } else if (!isVisible && items.lastOrNull() is Progress) {
            newData.removeAt(newData.lastIndex)
            items = newData
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any?>
    ) {
        super.onBindViewHolder(holder, position, payloads)

        if (position == items.lastIndex) nextPageListener()
    }

    class CatalogDiffCallback : DiffUtil.ItemCallback<Any>() {

        override fun areItemsTheSame(oldItem: Any, newItem: Any) = when {
            oldItem is Category && newItem is Category -> oldItem.id == newItem.id
            else -> oldItem::class.java == newItem::class.java
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any) = when {
            oldItem is Category && newItem is Category ->
                oldItem.name == newItem.name && oldItem.booksCount == newItem.booksCount

            oldItem is Header && newItem is Header ->
                oldItem.title == newItem.title

            else -> true
        }
    }
}