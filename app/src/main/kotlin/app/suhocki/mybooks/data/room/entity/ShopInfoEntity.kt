package app.suhocki.mybooks.data.room.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import app.suhocki.mybooks.data.firestore.FirestoreRepository
import app.suhocki.mybooks.domain.model.ShopInfo

@Entity(
    tableName = "ShopInfo",
    primaryKeys = ["id"]
)
class ShopInfoEntity @Ignore constructor() : ShopInfo {
    var id: String = FirestoreRepository.SHOP_INFO
    override lateinit var name: String
    override lateinit var phones: List<String>
    override lateinit var site: String
    override lateinit var vkGroup: String
    override lateinit var facebookGroup: String
    override lateinit var address: String
    override lateinit var schedule: String
    override lateinit var email: String

    constructor(
        name: String,
        phones: List<String>,
        site: String,
        vkGroup: String,
        facebookGroup: String,
        address: String,
        schedule: String,
        email: String
    ) : this() {
        this.name = name
        this.phones = phones
        this.site = site
        this.vkGroup = vkGroup
        this.facebookGroup = facebookGroup
        this.address = address
        this.schedule = schedule
        this.email = email
    }
}