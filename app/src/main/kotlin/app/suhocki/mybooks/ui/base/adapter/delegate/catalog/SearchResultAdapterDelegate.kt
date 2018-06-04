package app.suhocki.mybooks.ui.base.adapter.delegate.catalog

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import app.suhocki.mybooks.Analytics
import app.suhocki.mybooks.R
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.domain.model.SearchResult
import app.suhocki.mybooks.openLink
import app.suhocki.mybooks.ui.base.adapter.ui.catalog.SearchResultItemUI
import app.suhocki.mybooks.ui.base.listener.OnBookClickListener
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import com.squareup.picasso.Picasso
import org.jetbrains.anko.AnkoContext

class SearchResultAdapterDelegate(
    private val onBookClickListener: OnBookClickListener
) : AdapterDelegate<MutableList<Any>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        SearchResultItemUI()
            .apply { createView(AnkoContext.createReusable(parent.context, parent, false)) }
            .let { ViewHolder(it) }

    override fun isForViewType(items: MutableList<Any>, position: Int): Boolean =
        with(items[position]) { this is SearchResult }

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (holder as ViewHolder).bind(items[position] as SearchResult)


    private inner class ViewHolder(val ui: SearchResultItemUI) : RecyclerView.ViewHolder(ui.parent) {
        private lateinit var book: Book

        init {
            itemView.setOnClickListener { onBookClickListener.onBookClick(book) }
        }

        fun bind(searchResult: SearchResult) {
            this.book = searchResult.book
            with(ui) {
                price.text = parent.context.getString(R.string.rubles, book.price)
                buy.setOnClickListener {
                    Analytics.bookAddedToCart(book)
                    buy.context.openLink(book.website)
                }
                foundBy.text = searchResult.foundBy
                foundBy.text = searchResult.foundBy
                bookName.text = searchResult.book.shortName
                Picasso.get().load(searchResult.book.productLink).into(bookImage)
            }
        }
    }
}