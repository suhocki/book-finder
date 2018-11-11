package app.suhocki.mybooks.data.room.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import app.suhocki.mybooks.domain.model.statistics.PriceStatistics

@Entity(
    tableName = "PriceStatistics",
    indices = [(Index("categoryId"))],
    primaryKeys = ["categoryId"],
    foreignKeys = [
        (ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"]
        ))
    ]
)
data class PriceStatisticsEntity(
    override val categoryId: String,
    override val minPrice: Double,
    override val maxPrice: Double
) : PriceStatistics