package app.suhocki.mybooks.data.room.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import app.suhocki.mybooks.domain.model.statistics.AuthorStatistics

@Entity(
    indices = [(Index("category"))],
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