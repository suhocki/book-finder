package app.suhocki.mybooks.data.mapper.converter

import app.suhocki.mybooks.data.mapper.BaseConverter
import app.suhocki.mybooks.data.room.entity.ShopInfoEntity
import app.suhocki.mybooks.domain.model.Info
import app.suhocki.mybooks.domain.model.ShopInfo
import java.util.*
import javax.inject.Inject

class ContactsToShopInfo @Inject constructor() : BaseConverter<List<Info>, ShopInfo>(
    ArrayList<Info>().javaClass,
    ShopInfo::class.java
) {
    override fun convert(value: List<Info>): ShopInfo {
        return ShopInfoEntity(
            name = value.find { it.type == Info.InfoType.ORGANIZATION }!!.name,
            phones = value.asSequence()
                .filter { it.type == Info.InfoType.PHONE }
                .map { it.name }
                .toList(),
            site = value.find { it.type == Info.InfoType.WEBSITE }!!.name,
            vkGroup = value.find { it.type == Info.InfoType.VK }!!.name,
            facebookGroup = value.find { it.type == Info.InfoType.FACEBOOK }!!.name,
            address = value.find { it.type == Info.InfoType.ADDRESS }!!.name,
            schedule = value.find { it.type == Info.InfoType.WORKING_TIME }!!.name,
            email = value.find { it.type == Info.InfoType.EMAIL }!!.name
        )
    }
}