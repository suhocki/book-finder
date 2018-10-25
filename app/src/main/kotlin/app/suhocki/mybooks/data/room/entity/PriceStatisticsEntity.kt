package app.suhocki.mybooks.data.room.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import app.suhocki.mybooks.domain.model.statistics.PriceStatistics

@Entity(
    tableName = "PriceStatistics",
    indices = [(Index("category"))],
    primaryKeys = ["category"],
    foreignKeys = [
        (ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["name"],
            childColumns = ["category"]
        ))
    ]
)
data class PriceStatisticsEntity(
    override val category: String,
    override val minPrice: Double,
    override val maxPrice: Double
) : PriceStatistics