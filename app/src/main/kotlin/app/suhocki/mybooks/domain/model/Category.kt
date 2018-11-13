package app.suhocki.mybooks.domain.model

import android.os.Parcelable

interface Category: Parcelable {
    val id: String
    val name: String
    val booksCount: Int
}
