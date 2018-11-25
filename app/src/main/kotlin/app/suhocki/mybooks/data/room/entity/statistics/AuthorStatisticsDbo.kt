package app.suhocki.mybooks.data.room.entity.statistics

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import app.suhocki.mybooks.data.room.BooksDatabase
import app.suhocki.mybooks.data.room.entity.CategoryDbo
import app.suhocki.mybooks.domain.model.statistics.AuthorStatistics

@Entity(
    indices = [(Index("categoryId"))],
    tableName = BooksDatabase.Table.AUTHOR_STATISTICS,
    primaryKeys = ["author", "categoryId"],
    foreignKeys = [
        (ForeignKey(
            entity = CategoryDbo::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"]
        ))
    ]
)
data class AuthorStatisticsDbo(
    override val categoryId: String,
    override val author: String,
    override val count: Int
) : AuthorStatistics