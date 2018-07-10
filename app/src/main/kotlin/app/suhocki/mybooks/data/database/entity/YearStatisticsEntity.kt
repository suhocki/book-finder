package app.suhocki.mybooks.data.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import app.suhocki.mybooks.domain.model.statistics.YearStatistics

@Entity(
    tableName = "YearStatistics",
    indices = [(Index("category"))],
    primaryKeys = ["year", "category"],
    foreignKeys = [
        (ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["name"],
            childColumns = ["category"]
        ))
    ]
)
data class YearStatisticsEntity(
    override val category: String,
    override val year: String,
    override val count: Int
) : YearStatistics