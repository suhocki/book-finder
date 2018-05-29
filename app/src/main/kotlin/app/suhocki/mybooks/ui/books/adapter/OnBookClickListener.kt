package app.suhocki.mybooks.ui.books.adapter

import app.suhocki.mybooks.domain.model.Book

interface OnBookClickListener {
    fun onBookClick(book: Book)
}