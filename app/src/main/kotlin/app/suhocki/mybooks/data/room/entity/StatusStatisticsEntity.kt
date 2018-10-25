package app.suhocki.mybooks.data.room.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import app.suhocki.mybooks.domain.model.statistics.StatusStatistics

@Entity(
    tableName = "StatusStatistics",
    indices = [(Index("category"))],
    primaryKeys = ["status", "category"],
    foreignKeys = [
        (ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["name"],
            childColumns = ["category"]
        ))
    ]
)
data class StatusStatisticsEntity(
    override val category: String,
    override val status: String,
    override val count: Int
) : StatusStatistics