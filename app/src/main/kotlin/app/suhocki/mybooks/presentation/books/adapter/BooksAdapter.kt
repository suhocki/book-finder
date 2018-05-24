package app.suhocki.mybooks.presentation.books.adapter

import android.support.v7.recyclerview.extensions.AsyncListDiffer
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.domain.model.Book
import org.jetbrains.anko.AnkoContextImpl
import org.jetbrains.anko.AnkoLogger
import toothpick.Toothpick
import javax.inject.Inject


class BooksAdapter @Inject constructor() :
    RecyclerView.Adapter<BookViewHolder>(), AnkoLogger {

    private lateinit var differ: AsyncListDiffer<Book>
    private var onBookClickListener: OnBookClickListener? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        if (!this::differ.isInitialized) differ =
                Toothpick.openScopes(DI.APP_SCOPE, DI.BOOKS_ACTIVITY_SCOPE)
                    .getInstance(BooksDiffer::class.java).get()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val viewHolder = BookViewHolder(BookItemUI().apply {
            createView(AnkoContextImpl(parent.context, parent, false))
        })

        viewHolder.itemView.setOnClickListener {
            val book = differ.currentList[viewHolder.adapterPosition]
            onBookClickListener?.onBookClick(book)
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

    fun setOnBookClickListener(onBookClickListener: OnBookClickListener?) {
        this.onBookClickListener = onBookClickListener
    }
}