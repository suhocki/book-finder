package app.suhocki.mybooks.data.room.entity.statistics

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import app.suhocki.mybooks.data.room.BooksDatabase
import app.suhocki.mybooks.data.room.entity.CategoryDbo
import app.suhocki.mybooks.domain.model.statistics.StatusStatistics

@Entity(
    tableName = BooksDatabase.Table.STATUS_STATISTICS,
    indices = [(Index("categoryId"))],
    primaryKeys = ["status", "categoryId"],
    foreignKeys = [
        (ForeignKey(
            entity = CategoryDbo::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"]
        ))
    ]
)
data class StatusStatisticsDbo(
    override val categoryId: String,
    override val status: String,
    override val count: Int
) : StatusStatistics