package app.suhocki.mybooks.data.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.os.Parcel
import android.os.Parcelable
import app.suhocki.mybooks.domain.model.Category

@Entity(
    tableName = "Categories",
    primaryKeys = ["name"]
)
data class CategoryEntity(
    override var name: String,
    override var booksCount: Int = 0
) : Category {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(booksCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CategoryEntity> {
        override fun createFromParcel(parcel: Parcel): CategoryEntity {
            return CategoryEntity(parcel)
        }

        override fun newArray(size: Int): Array<CategoryEntity?> {
            return arrayOfNulls(size)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Category) return false
        if (System.identityHashCode(other) == System.identityHashCode(this)) return true
        return other.name == name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}