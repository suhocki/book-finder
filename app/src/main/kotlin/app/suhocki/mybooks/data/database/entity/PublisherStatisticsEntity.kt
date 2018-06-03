package app.suhocki.mybooks.data.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import app.suhocki.mybooks.domain.model.statistics.PublisherStatistics

@Entity(
    tableName = "PublisherStatistics",
    primaryKeys = ["publisher"],
    foreignKeys = [
        (ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["name"],
            childColumns = ["category"]
        ))
    ]
)
data class PublisherStatisticsEntity(
    override val category: String,
    override val publisher: String,
    override val count: Int
) : PublisherStatistics