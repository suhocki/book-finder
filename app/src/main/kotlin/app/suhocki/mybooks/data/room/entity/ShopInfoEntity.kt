package app.suhocki.mybooks.data.room.entity

import android.arch.persistence.room.Entity
import app.suhocki.mybooks.domain.model.ShopInfo

@Entity(
    tableName = "ShopInfo",
    primaryKeys = ["id"]
)
data class ShopInfoEntity(
    val id: String = "shopInfo",
    override val name: String,
    override val phones: List<String>,
    override val site: String,
    override val vkGroup: String,
    override val facebookGroup: String,
    override val address: String,
    override val schedule: String,
    override val email: String
) : ShopInfo