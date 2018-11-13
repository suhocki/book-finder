package app.suhocki.mybooks.ui.books.delegate

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import app.suhocki.mybooks.R
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.toRoundedPrice
import app.suhocki.mybooks.ui.base.entity.BookEntity
import app.suhocki.mybooks.ui.base.listener.OnBookClickListener
import app.suhocki.mybooks.ui.books.ui.BookItemUI
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import org.jetbrains.anko.AnkoContext



class BookAdapterDelegate(
    private val onBookClickListener: OnBookClickListener
) : AdapterDelegate<MutableList<Any>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        BookItemUI()
            .apply { createView(AnkoContext.createReusable(parent.context, parent, false)) }
            .let { ViewHolder(it) }

    override fun isForViewType(items: MutableList<Any>, position: Int): Boolean =
        with(items[position]) { this is Book }

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (holder as ViewHolder).bind(items[position] as BookEntity, payloads)


    private inner class ViewHolder(val ui: BookItemUI) : RecyclerView.ViewHolder(ui.parent) {
        private lateinit var book: BookEntity

        init {
            itemView.setOnClickListener { onBookClickListener.onBookClick(book) }
            ui.buy.setOnClickListener { onBookClickListener.onBuyBookClick(book) }
        }

        fun bind(book: BookEntity, payloads: MutableList<Any>) {
            this.book = book
            with(ui) {
                icon.setImageURI(book.iconLink)
                name.text = book.shortName
                price.text = parent.context.getString(R.string.rubles, book.price.toRoundedPrice())
                buy.setImageResource(book.buyDrawableRes)
                if (payloads.firstOrNull() is Int) {
                    val drawableRes = payloads.first() as Int
                    buy.setImageResource(drawableRes)
                    book.buyDrawableRes = drawableRes
                }
            }
        }
    }
}