package app.suhocki.mybooks.data.mapper.converter

import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.mapper.BaseConverter
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.domain.model.Header
import app.suhocki.mybooks.domain.model.Info
import app.suhocki.mybooks.domain.model.ShopInfo
import java.util.*
import javax.inject.Inject

class ShopInfoToList @Inject constructor(
    private val resourceManager: ResourceManager
) : BaseConverter<ShopInfo, List<Any>>(
    ShopInfo::class.java,
    ArrayList<Any>().javaClass
) {
    override fun convert(value: ShopInfo) =
        mutableListOf<Any>().apply {
            add(HeaderEntity(value.name, true))

            addAll(value.phones
                .map { ContactEntity(Info.InfoType.PHONE, it, R.drawable.ic_phone) })

            var title = value.site
            val valueForNavigation = resourceManager.getString(R.string.website_url)
            add(ContactEntity(Info.InfoType.WEBSITE, title, R.drawable.ic_web, valueForNavigation))
            title = resourceManager.getString(R.string.facebook_group)
            add(ContactEntity(Info.InfoType.FACEBOOK, title, R.drawable.ic_fb, value.facebookGroup))
            title = resourceManager.getString(R.string.vk_group)
            add(ContactEntity(Info.InfoType.VK, title, R.drawable.ic_vk, value.vkGroup))
            add(ContactEntity(Info.InfoType.EMAIL, value.email, R.drawable.ic_email))

            add(HeaderEntity(resourceManager.getString(R.string.address), true))
            add(ContactEntity(Info.InfoType.ADDRESS, value.address, R.drawable.ic_address))

            add(HeaderEntity(resourceManager.getString(R.string.working_time), true))
            add(ContactEntity(Info.InfoType.WORKING_TIME, value.schedule, R.drawable.ic_time))
        }

    inner class HeaderEntity(
        override var title: String,
        override val inverseColors: Boolean = false,
        override val allCaps: Boolean = true
    ) : Header

    inner class ContactEntity(
        override val type: Info.InfoType,
        override val name: String,
        override val iconRes: Int,
        override val valueForNavigation: String? = null
    ) : Info
}