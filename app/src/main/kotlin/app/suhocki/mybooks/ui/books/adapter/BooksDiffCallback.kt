package app.suhocki.mybooks.ui.books.adapter

import android.support.v7.util.DiffUtil
import app.suhocki.mybooks.domain.model.Book

internal class BooksDiffCallback : DiffUtil.ItemCallback<Book>() {
    override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem.productCode == newItem.productCode
    }

    override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem.productCode == newItem.productCode
    }
}