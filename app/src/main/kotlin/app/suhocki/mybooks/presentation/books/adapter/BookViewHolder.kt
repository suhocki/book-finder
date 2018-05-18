package app.suhocki.mybooks.presentation.books.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import com.squareup.picasso.Picasso
import app.suhocki.mybooks.R
import app.suhocki.mybooks.domain.model.Book


class BookViewHolder(
    private val layout: BookItemUI,
    private val context: Context = layout.parent.context
) : RecyclerView.ViewHolder(layout.parent) {

    fun bind(book: Book) {
        Picasso.get().load(book.iconLink).into(layout.icon)
        layout.name.text = book.shortName
        layout.price.text = context.getString(R.string.rubles, book.price)
    }
}