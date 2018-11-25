package app.suhocki.mybooks.data.room.entity.statistics

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import app.suhocki.mybooks.data.room.BooksDatabase
import app.suhocki.mybooks.data.room.entity.CategoryDbo
import app.suhocki.mybooks.domain.model.statistics.PublisherStatistics

@Entity(
    tableName = BooksDatabase.Table.PUBLISHER_STATISTICS,
    indices = [(Index("categoryId"))],
    primaryKeys = ["publisher", "categoryId"],
    foreignKeys = [
        (ForeignKey(
            entity = CategoryDbo::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"]
        ))
    ]
)
data class PublisherStatisticsDbo(
    override val categoryId: String,
    override val publisher: String,
    override val count: Int
) : PublisherStatistics