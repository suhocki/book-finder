package app.suhocki.mybooks.ui.books.adapter

import android.support.v7.recyclerview.extensions.AsyncListDiffer
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import app.suhocki.mybooks.domain.model.Book
import org.jetbrains.anko.AnkoContextImpl
import org.jetbrains.anko.AnkoLogger


class BooksAdapter(
    private val onBookClickListener: OnBookClickListener
) : RecyclerView.Adapter<BookViewHolder>(), AnkoLogger {

    private var differ = AsyncListDiffer(this, BooksDiffCallback())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val viewHolder = BookViewHolder(BookItemUI().apply {
            createView(AnkoContextImpl(parent.context, parent, false))
        })

        viewHolder.itemView.setOnClickListener {
            val book = differ.currentList[viewHolder.adapterPosition]
            onBookClickListener.onBookClick(book)
        }
        return viewHolder
    }

    override fun getItemCount(): Int =
        differ.currentList.size

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.layout.book = differ.currentList[position]
    }

    fun submitList(list: List<Book>) =
        mutableListOf<Book>().apply {
            addAll(list)
            differ.submitList(this)
        }
}