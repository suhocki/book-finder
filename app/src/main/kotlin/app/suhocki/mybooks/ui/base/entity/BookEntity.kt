package app.suhocki.mybooks.ui.base.entity

import android.os.Parcel
import android.os.Parcelable
import app.suhocki.mybooks.R
import app.suhocki.mybooks.domain.model.Book

class BookEntity(
    override val category: String,
    override val shortName: String,
    override val fullName: String,
    override val price: Double,
    override val iconLink: String,
    override val productLink: String,
    override val productCode: String,
    override val website: String,
    override val status: String?,
    override val publisher: String?,
    override val author: String?,
    override val series: String?,
    override val format: String?,
    override val year: String?,
    override val pageCount: String?,
    override val cover: String?,
    override val description: String?,
    var buyDrawableRes: Int = R.drawable.ic_buy
) : Book {

    constructor(book: Book): this(
        book.category,
        book.shortName,
        book.fullName,
        book.price,
        book.iconLink,
        book.productLink,
        book.productCode,
        book.website,
        book.status,
        book.publisher,
        book.author,
        book.series,
        book.format,
        book.year,
        book.pageCount,
        book.cover,
        book.description
    )

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(category)
        parcel.writeString(shortName)
        parcel.writeString(fullName)
        parcel.writeDouble(price)
        parcel.writeString(iconLink)
        parcel.writeString(productLink)
        parcel.writeString(productCode)
        parcel.writeString(website)
        parcel.writeString(status)
        parcel.writeString(publisher)
        parcel.writeString(author)
        parcel.writeString(series)
        parcel.writeString(format)
        parcel.writeString(year)
        parcel.writeString(pageCount)
        parcel.writeString(cover)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BookEntity> {
        override fun createFromParcel(parcel: Parcel): BookEntity {
            return BookEntity(parcel)
        }

        override fun newArray(size: Int): Array<BookEntity?> {
            return arrayOfNulls(size)
        }
    }
}