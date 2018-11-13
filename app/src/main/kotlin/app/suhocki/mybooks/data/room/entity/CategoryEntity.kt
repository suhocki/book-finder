package app.suhocki.mybooks.data.room.entity

import android.arch.persistence.room.Entity
import android.os.Parcel
import android.os.Parcelable
import app.suhocki.mybooks.domain.model.Category

@Entity(
    tableName = "Categories",
    primaryKeys = ["id"]
)
class CategoryEntity() : Category {

    override lateinit var id: String
    override lateinit var name: String
    override var booksCount: Int = 0

    constructor(
        id: String,
        name: String,
        booksCount: Int = 0
    ) : this() {
        this.id = id
        this.name = name
        this.booksCount = booksCount
    }

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
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
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CategoryEntity

        if (id != other.id) return false
        if (name != other.name) return false
        if (booksCount != other.booksCount) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}