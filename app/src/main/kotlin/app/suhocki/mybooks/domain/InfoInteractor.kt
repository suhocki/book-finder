package app.suhocki.mybooks.domain

import app.suhocki.mybooks.R
import app.suhocki.mybooks.domain.model.Contact
import app.suhocki.mybooks.domain.model.Header
import app.suhocki.mybooks.domain.repository.InfoRepository
import javax.inject.Inject

class InfoInteractor @Inject constructor(
    private val infoRepository: InfoRepository
) {

    fun getHeaderOrganization() =
        infoRepository.getOrganizationName()
            .let {
                object : Header {
                    override var title = it
                }
            }

    fun getContacts(): List<Contact> {
        val phones = infoRepository.getContactPhones()
            .map { ContactEntity(Contact.ContactType.PHONE, it, R.drawable.ic_phone) }
        val website = infoRepository.getWebsite()
            .let { ContactEntity(Contact.ContactType.WEBSITE, it, R.drawable.ic_web) }
        val email = infoRepository.getContactEmail()
            .let { ContactEntity(Contact.ContactType.EMAIL, it, R.drawable.ic_email) }
        val vkGroup = infoRepository.getVkGroup()
            .let { (title, url) ->
                ContactEntity(Contact.ContactType.VK, title, R.drawable.ic_vk, url)
            }
        val facebook = infoRepository.getFacebook()
            .let { (title, url) ->
                ContactEntity(Contact.ContactType.FACEBOOK, title, R.drawable.ic_facebook, url)
            }
        return mutableListOf<Contact>().apply {
            addAll(phones)
            add(website)
            add(email)
            add(vkGroup)
            add(facebook)
        }
    }

    internal class ContactEntity(
        override val type: Contact.ContactType,
        override val name: String,
        override val iconRes: Int,
        override val valueForNavigation: String? = null
    ) : Contact

}