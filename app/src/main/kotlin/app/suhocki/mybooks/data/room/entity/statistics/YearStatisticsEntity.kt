package app.suhocki.mybooks.data.room.entity.statistics

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import app.suhocki.mybooks.data.room.BooksDatabase
import app.suhocki.mybooks.data.room.entity.CategoryEntity
import app.suhocki.mybooks.domain.model.statistics.YearStatistics

@Entity(
    tableName = BooksDatabase.Table.YEAR_STATISTICS,
    indices = [(Index("categoryId"))],
    primaryKeys = ["year", "categoryId"],
    foreignKeys = [
        (ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"]
        ))
    ]
)
data class YearStatisticsEntity(
    override val categoryId: String,
    override val year: String,
    override val count: Int
) : YearStatistics