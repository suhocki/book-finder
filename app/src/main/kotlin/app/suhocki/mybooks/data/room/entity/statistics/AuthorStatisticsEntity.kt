package app.suhocki.mybooks.data.room.entity.statistics

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import app.suhocki.mybooks.data.room.BooksDatabase
import app.suhocki.mybooks.data.room.entity.CategoryEntity
import app.suhocki.mybooks.domain.model.statistics.AuthorStatistics

@Entity(
    indices = [(Index("categoryId"))],
    tableName = BooksDatabase.Table.AUTHOR_STATISTICS,
    primaryKeys = ["author", "categoryId"],
    foreignKeys = [
        (ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"]
        ))
    ]
)
data class AuthorStatisticsEntity(
    override val categoryId: String,
    override val author: String,
    override val count: Int
) : AuthorStatistics