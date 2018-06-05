package app.suhocki.mybooks.data.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import app.suhocki.mybooks.domain.model.statistics.StatusStatistics

@Entity(
    tableName = "StatusStatistics",
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