package app.suhocki.mybooks.domain.model.filter

import android.os.Parcelable

interface FilterAuthor : Parcelable, Checkable {
    val authorName: String
    val booksCount: Int
}