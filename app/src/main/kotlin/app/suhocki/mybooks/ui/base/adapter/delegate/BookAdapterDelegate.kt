package app.suhocki.mybooks.ui.base.adapter.delegate

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import app.suhocki.mybooks.Analytics
import app.suhocki.mybooks.R
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.openLink
import app.suhocki.mybooks.ui.base.adapter.listener.OnBookClickListener
import app.suhocki.mybooks.ui.base.adapter.ui.BookItemUI
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import com.squareup.picasso.Picasso
import org.jetbrains.anko.AnkoContextImpl

class BookAdapterDelegate(
    private val onBookClickListener: OnBookClickListener
) : AdapterDelegate<MutableList<Any>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        BookItemUI()
            .apply { createView(AnkoContextImpl(parent.context, parent, false)) }
            .let { ViewHolder(it) }

    override fun isForViewType(items: MutableList<Any>, position: Int): Boolean =
        with(items[position]) { this is Book }

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (holder as ViewHolder).bind(items[position] as Book)


    private inner class ViewHolder(val ui: BookItemUI) : RecyclerView.ViewHolder(ui.parent) {
        private lateinit var book: Book

        init {
            itemView.setOnClickListener { onBookClickListener.onBookClick(book) }
        }

        fun bind(book: Book) {
            this.book = book
            with(ui) {
                Picasso.get().load(book.iconLink).into(icon)
                name.text = book.shortName
                price.text = parent.context.getString(R.string.rubles, book.price)
                buy.setOnClickListener {
                    Analytics.bookAddedToCart(book)
                    buy.context.openLink(book.website)
                }
            }
        }
    }
}