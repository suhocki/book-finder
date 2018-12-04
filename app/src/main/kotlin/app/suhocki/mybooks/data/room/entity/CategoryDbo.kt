package app.suhocki.mybooks.data.room.entity

import android.arch.persistence.room.Entity
import app.suhocki.mybooks.data.room.BooksDatabase
import app.suhocki.mybooks.domain.model.Category

@Entity(
    tableName = BooksDatabase.Table.CATEGORIES,
    primaryKeys = [CategoryDbo.ID]
)
data class CategoryDbo constructor(
    override var id: String = "",
    override var name: String = "",
    var creationDate: String = "",
    override var booksCount: Int = 0
) : Category {

    companion object {
        const val ID = "id"
    }
}