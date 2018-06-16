package app.suhocki.mybooks.data.database.entity

import android.arch.persistence.room.Entity
import app.suhocki.mybooks.domain.model.Banner

@Entity(
    tableName = "Banners",
    primaryKeys = ["imageUrl"]
)
data class BannerEntity(
    override val imageUrl: String,
    override val description: String
) : Banner