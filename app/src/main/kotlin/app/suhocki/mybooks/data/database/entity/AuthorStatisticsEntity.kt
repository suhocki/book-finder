package app.suhocki.mybooks.data.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import app.suhocki.mybooks.domain.model.statistics.AuthorStatistics

@Entity(
    tableName = "AuthorStatistics",
    primaryKeys = ["author", "category"],
    foreignKeys = [
        (ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["name"],
            childColumns = ["category"]
        ))
    ]
)
data class AuthorStatisticsEntity(
    override val category: String,
    override val author: String,
    override val count: Int
) : AuthorStatistics