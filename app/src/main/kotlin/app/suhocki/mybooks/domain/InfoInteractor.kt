package app.suhocki.mybooks.domain

import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.domain.model.Header
import app.suhocki.mybooks.domain.model.Info
import app.suhocki.mybooks.domain.repository.InfoRepository
import javax.inject.Inject

class InfoInteractor @Inject constructor(
    private val infoRepository: InfoRepository,
    private val resourceManager: ResourceManager
) {

    fun getHeaderOrganization() =
        infoRepository.getOrganizationName()
            .let {
                object : Header {
                    override val inverseColors = true
                    override var title = it
                }
            }

    fun getHeaderAddress() =
        object : Header {
            override val inverseColors = true
            override var title = resourceManager.getString(R.string.address)
        }

    fun getHeaderWorkingTime() =
        object : Header {
            override val inverseColors = true
            override var title = resourceManager.getString(R.string.working_time)
        }

    fun getContacts(): List<Info> {
        val phones = infoRepository.getContactPhones()
            .map { ContactEntity(Info.InfoType.PHONE, it, R.drawable.ic_phone) }
        val website = infoRepository.getWebsite()
            .let { (websiteName, url) ->
                ContactEntity(Info.InfoType.WEBSITE, websiteName, R.drawable.ic_web, url)
            }
        val email = infoRepository.getContactEmail()
            .let { ContactEntity(Info.InfoType.EMAIL, it, R.drawable.ic_email) }
        val vkGroup = infoRepository.getVkGroup()
            .let { (title, url) ->
                ContactEntity(Info.InfoType.VK, title, R.drawable.ic_vk, url)
            }
        val facebook = infoRepository.getFacebook()
            .let { (title, url) ->
                ContactEntity(Info.InfoType.FACEBOOK, title, R.drawable.ic_facebook, url)
            }
        return mutableListOf<Info>().apply {
            addAll(phones)
            add(website)
            add(email)
            add(vkGroup)
            add(facebook)
        }
    }

    fun getWorkingTime(): Info = infoRepository.getWorkingTime()
        .let { ContactEntity(Info.InfoType.WORKING_TIME, it, R.drawable.ic_time) }

    fun getAddress(): Info = infoRepository.getAddress()
        .let { ContactEntity(Info.InfoType.ADDRESS, it, R.drawable.ic_address) }

    internal class ContactEntity(
        override val type: Info.InfoType,
        override val name: String,
        override val iconRes: Int,
        override val valueForNavigation: String? = null
    ) : Info

}