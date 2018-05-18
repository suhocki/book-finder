package app.suhocki.mybooks.presentation.books.adapter

import app.suhocki.mybooks.domain.model.Book

interface OnBookClickListener {
    fun onBookClick(book: Book)
}