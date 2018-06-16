package app.suhocki.mybooks.ui.catalog.delegate

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import app.suhocki.mybooks.R
import app.suhocki.mybooks.domain.model.SearchResult
import app.suhocki.mybooks.ui.base.entity.BookEntity
import app.suhocki.mybooks.ui.base.listener.OnBookClickListener
import app.suhocki.mybooks.ui.catalog.ui.SearchResultItemUI
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import com.squareup.picasso.Picasso
import org.jetbrains.anko.AnkoContext

class SearchResultBookAdapterDelegate(
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
    ) = (holder as ViewHolder).bind(items[position] as SearchResult, payloads)


    private inner class ViewHolder(val ui: SearchResultItemUI) :
        RecyclerView.ViewHolder(ui.parent) {
        private lateinit var book: BookEntity

        init {
            itemView.setOnClickListener { onBookClickListener.onBookClick(book) }
            ui.buy.setOnClickListener { onBookClickListener.onBuyBookClick(book) }
        }

        fun bind(searchResult: SearchResult, payloads: MutableList<Any>) {
            this.book = searchResult.book as BookEntity
            with(ui) {
                if (payloads.isNotEmpty()) {
                    val drawableRes = payloads.first() as Int
                    buy.setImageResource(drawableRes)
                    book.buyDrawableRes = drawableRes
                }
                price.text = parent.context.getString(R.string.rubles, book.price)
                buy.setImageResource(book.buyDrawableRes)
                foundBy.text = searchResult.foundBy
                foundBy.text = searchResult.foundBy
                bookName.text = searchResult.book.shortName
                Picasso.get().load(searchResult.book.productLink).into(bookImage)
            }
        }
    }
}