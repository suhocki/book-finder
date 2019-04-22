package app.suhocki.mybooks.ui.search

import android.support.v7.util.DiffUtil
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.domain.model.BookSearchResult
import app.suhocki.mybooks.ui.base.delegate.ProgressAdapterDelegate
import app.suhocki.mybooks.ui.search.delegate.BookSearchResultAdapterDelegate
import com.hannesdorfmann.adapterdelegates3.AsyncListDifferDelegationAdapter

class SearchAdapter(
    diffCallback: SearchDiffCallback,
    bookClickListener: (Book) -> Unit
) : AsyncListDifferDelegationAdapter<Any>(diffCallback) {

    init {
        delegatesManager
            .addDelegate(BookSearchResultAdapterDelegate(bookClickListener))
            .addDelegate(ProgressAdapterDelegate())
    }

    fun setData(list: List<Any>) {
        items = list.toList()
    }

    class SearchDiffCallback : DiffUtil.ItemCallback<Any>() {

        override fun areItemsTheSame(oldItem: Any, newItem: Any) = when {
            oldItem is BookSearchResult && newItem is BookSearchResult -> oldItem.book.id == newItem.book.id

            else -> oldItem::class.java == newItem::class.java
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any) = true
    }
}