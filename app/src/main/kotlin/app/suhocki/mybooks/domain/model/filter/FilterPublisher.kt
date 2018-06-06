package app.suhocki.mybooks.domain.model.filter

import android.os.Parcelable

interface FilterPublisher : Parcelable, Checkable {
    val publisherName: String
    val booksCount: Int
}