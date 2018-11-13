package app.suhocki.mybooks.data.room.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.Index
import android.os.Parcel
import android.os.Parcelable
import app.suhocki.mybooks.domain.model.Book

@Entity(
    indices = [(Index("categoryId"))],
    tableName = BookEntity.TABLE_NAME,
    primaryKeys = ["productCode"],
    foreignKeys = [
        (ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"]
        ))
    ]
)
data class BookEntity(
    override var categoryId: String = "",
    override var shortName: String = "",
    override var fullName: String = "",
    override var price: Double = 0.0,
    override var iconLink: String = "",
    override var productLink: String = "",
    override var website: String = "",
    override var productCode: String = "",
    override var status: String? = null,
    override var publisher: String? = null,
    override var author: String? = null,
    override var series: String? = null,
    override var format: String? = null,
    override var year: String? = null,
    override var pageCount: String? = null,
    override var cover: String? = null,
    override var description: String? = null,
    @Ignore var shortDescription: String = "",
    @Ignore var fullDescription: String = ""
) : Book {
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
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(categoryId)
        parcel.writeString(shortName)
        parcel.writeString(fullName)
        parcel.writeDouble(price)
        parcel.writeString(iconLink)
        parcel.writeString(productLink)
        parcel.writeString(website)
        parcel.writeString(productCode)
        parcel.writeString(status)
        parcel.writeString(publisher)
        parcel.writeString(author)
        parcel.writeString(series)
        parcel.writeString(format)
        parcel.writeString(year)
        parcel.writeString(pageCount)
        parcel.writeString(cover)
        parcel.writeString(description)
        parcel.writeString(shortDescription)
        parcel.writeString(fullDescription)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BookEntity> {
        const val FIELD_STATUS = "status"
        const val FIELD_YEAR = "year"
        const val FIELD_AUTHOR = "author"
        const val FIELD_PUBLISHER = "publisher"
        const val FIELD_SHORT_NAME = "shortName"
        const val FIELD_PRICE = "price"
        const val FIELD_CATEGORY = "categoryId"
        const val TABLE_NAME = "Books"
        override fun createFromParcel(parcel: Parcel): BookEntity {
            return BookEntity(parcel)
        }

        override fun newArray(size: Int): Array<BookEntity?> {
            return arrayOfNulls(size)
        }
    }
}