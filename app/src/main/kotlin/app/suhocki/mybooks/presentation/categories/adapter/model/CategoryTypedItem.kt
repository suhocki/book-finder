package app.suhocki.mybooks.presentation.categories.adapter.model

import android.os.Parcel
import android.os.Parcelable
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.model.TypedItem
import app.suhocki.mybooks.presentation.categories.adapter.ItemType

class CategoryTypedItem(
    override val name: String,
    override val booksCount: Int,
    override val type: ItemType = ItemType.CATEGORY
) : Category, TypedItem {

    constructor(category: Category) : this(category.name, category.booksCount)

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readSerializable() as ItemType
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(booksCount)
        parcel.writeSerializable(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CategoryTypedItem> {
        override fun createFromParcel(parcel: Parcel): CategoryTypedItem {
            return CategoryTypedItem(parcel)
        }

        override fun newArray(size: Int): Array<CategoryTypedItem?> {
            return arrayOfNulls(size)
        }
    }
}