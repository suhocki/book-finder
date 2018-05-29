package app.suhocki.mybooks.ui.base.adapter.listener

import app.suhocki.mybooks.domain.model.Book

interface OnBookClickListener {
    fun onBookClick(book: Book)
}