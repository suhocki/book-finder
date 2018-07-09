package app.suhocki.mybooks.domain

import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.domain.model.Info
import app.suhocki.mybooks.domain.repository.InfoRepository
import app.suhocki.mybooks.ui.info.entity.ContactEntity
import app.suhocki.mybooks.ui.info.entity.HeaderEntity
import app.suhocki.mybooks.ui.info.entity.InfoEntity
import javax.inject.Inject

class InfoInteractor @Inject constructor(
    private val infoRepository: InfoRepository,
    private val resourceManager: ResourceManager
) {

    fun getHeaderOrganization() =
        infoRepository.getOrganizationName()
            .let { HeaderEntity(it, true) }

    fun getHeaderAddress() = HeaderEntity(
        resourceManager.getString(R.string.address),
        true
    )

    fun getHeaderWorkingTime() = HeaderEntity(
        resourceManager.getString(R.string.working_time),
        true
    )

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

    fun getAboutThisApplication() = listOf(
        HeaderEntity(
            resourceManager.getString(R.string.about_this_application),
            true
        ),
        InfoEntity(
            Info.InfoType.ABOUT_DEVELOPER,
            resourceManager.getString(R.string.about_developer),
            iconRes = R.drawable.ic_developer
        ),
        InfoEntity(
            Info.InfoType.LICENSES,
            resourceManager.getString(R.string.licences),
            iconRes = R.drawable.ic_copyright
        )
    )

}