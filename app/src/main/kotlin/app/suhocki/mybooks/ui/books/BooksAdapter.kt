package app.suhocki.mybooks.ui.books

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.ui.base.delegate.ProgressAdapterDelegate
import app.suhocki.mybooks.ui.base.entity.Progress
import app.suhocki.mybooks.ui.books.delegate.BookAdapterDelegate
import com.hannesdorfmann.adapterdelegates3.AsyncListDifferDelegationAdapter

class BooksAdapter(
    diffCallback: BooksDiffCallback,
    onBookClick: (Book) -> Unit,
    private val nextPageListener: () -> Unit
) : AsyncListDifferDelegationAdapter<Any>(diffCallback) {

    init {
        delegatesManager
            .addDelegate(BookAdapterDelegate(onBookClick))
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

    class BooksDiffCallback : DiffUtil.ItemCallback<Any>() {

        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return if (oldItem is Book && newItem is Book) oldItem.id == newItem.id
            else false
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any) =
            when {
                oldItem is Book && newItem is Book ->
                    oldItem.shortName == newItem.shortName &&
                            oldItem.price == newItem.price &&
                            oldItem.iconLink == newItem.iconLink

                else -> false
            }
    }
}