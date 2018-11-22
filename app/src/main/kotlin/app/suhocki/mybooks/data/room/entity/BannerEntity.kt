package app.suhocki.mybooks.data.room.entity

import android.arch.persistence.room.Entity
import app.suhocki.mybooks.data.room.BooksDatabase
import app.suhocki.mybooks.domain.model.Banner

@Entity(
    tableName = BooksDatabase.Table.BANNERS,
    primaryKeys = ["imageUrl"]
)
data class BannerEntity(
    override val id: String,
    override val imageUrl: String,
    override val description: String
) : Banner