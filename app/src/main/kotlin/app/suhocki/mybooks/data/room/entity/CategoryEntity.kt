package app.suhocki.mybooks.data.room.entity

import android.arch.persistence.room.Entity
import app.suhocki.mybooks.data.room.BooksDatabase
import app.suhocki.mybooks.domain.model.Category

@Entity(
    tableName = BooksDatabase.Table.CATEGORIES,
    primaryKeys = [CategoryEntity.ID]
)
data class CategoryEntity constructor(
    override var id: String,
    override var name: String,
    override var booksCount: Int = 0
) : Category {

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

    companion object {
        const val ID = "id"
    }
}