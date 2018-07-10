package app.suhocki.mybooks.data.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import app.suhocki.mybooks.domain.model.statistics.PublisherStatistics

@Entity(
    tableName = "PublisherStatistics",
    indices = [(Index("category"))],
    primaryKeys = ["publisher", "category"],
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