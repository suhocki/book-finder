package app.suhocki.mybooks.ui.books

import android.support.v7.recyclerview.extensions.AsyncDifferConfig
import android.support.v7.recyclerview.extensions.AsyncListDiffer
import app.suhocki.mybooks.ui.base.EndActionAdapterListUpdateCallback
import app.suhocki.mybooks.ui.base.listener.OnBookClickListener
import app.suhocki.mybooks.ui.books.delegate.BookAdapterDelegate
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter


class BooksAdapter(
    onBookClickListener: OnBookClickListener
) : ListDelegationAdapter<MutableList<Any>>() {

    private val listUpdateCallback by lazy { EndActionAdapterListUpdateCallback(this, null) }

    private val diffConfig by lazy { AsyncDifferConfig.Builder<Any>(BooksDiffCallback()).build() }

    private val differ by lazy { AsyncListDiffer(listUpdateCallback, diffConfig) }

    init {
        delegatesManager.addDelegate(BookAdapterDelegate(onBookClickListener))
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