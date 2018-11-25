package app.suhocki.mybooks.ui.base.listener

import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.ui.base.entity.UiBook

interface OnBookClickListener {
    fun onBookClick(book: Book)

    fun onBuyBookClick(book: UiBook)
}