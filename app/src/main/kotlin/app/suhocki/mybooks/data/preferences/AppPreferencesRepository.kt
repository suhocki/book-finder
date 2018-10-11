package app.suhocki.mybooks.data.preferences

import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.domain.repository.InfoRepository
import app.suhocki.mybooks.domain.repository.SettingsRepository
import net.grandcentrix.tray.AppPreferences
import javax.inject.Inject

class AppPreferencesRepository @Inject constructor(
    private val preferences: AppPreferences,
    private val resourceManager: ResourceManager
) : SettingsRepository, InfoRepository {

    override var isAdminModeEnabled: Boolean
        get() = preferences.getBoolean(IS_ADMIN_ENABLED, false)
        set(value) {
            preferences.put(IS_ADMIN_ENABLED, value)
        }

    override fun getOrganizationName(): String? =
        preferences.getString(ORGANIZATION_NAME, null)

    override fun setOrganizationName(name: String) {
        preferences.put(ORGANIZATION_NAME, name)
    }

    override fun getContactPhones(): Set<String>? {
        val phone1 = preferences.getString(CONTACT_PHONE_1, null)
        val phone2 = preferences.getString(CONTACT_PHONE_2, null)

        if (phone1 == null && phone2 == null) return null
        return mutableSetOf<String>().apply {
            phone1?.let { add(it) }
            phone2?.let { add(it) }
        }
    }

    override fun setContactPhones(phones: Set<String>) {
        phones.forEachIndexed { index, item ->
            if (index == 0) preferences.put(CONTACT_PHONE_1, item)
            if (index == 1) preferences.put(CONTACT_PHONE_2, item)
        }
    }

    override fun getContactEmail(): String? =
        preferences.getString(CONTACT_EMAIL, null)

    override fun setContactEmail(email: String) {
        preferences.put(CONTACT_EMAIL, email)
    }

    override fun getWebsite(): Pair<String, String>? = with(preferences) {
        if (contains(WEBSITE)) preferences.getString(WEBSITE)!! to
                resourceManager.getString(R.string.website_url)
        else null
    }

    override fun setWebsite(website: String) {
        preferences.put(WEBSITE, website)
    }

    override fun getVkGroup(): Pair<String, String>? = with(preferences) {
        if (contains(VK_GROUP_NAME) && contains(VK_GROUP_WEBSITE))
            getString(VK_GROUP_NAME)!! to getString(VK_GROUP_WEBSITE)!!
        else null
    }

    override fun setVkGroup(url: String) {
        preferences.put(VK_GROUP_WEBSITE, url)
    }

    override fun getFacebook(): Pair<String, String>? = with(preferences) {
        if (contains(FACEBOOK_NAME) && contains(FACEBOOK_WEBSITE))
            getString(FACEBOOK_NAME)!! to getString(FACEBOOK_WEBSITE)!!
        else null
    }

    override fun setFacebook(url: String) {
        preferences.put(FACEBOOK_WEBSITE, url)
    }

    override fun getAddress(): String? =
        preferences.getString(ADDRESS, null)

    override fun setAddress(address: String) {
        preferences.put(ADDRESS, address)
    }

    override fun getWorkingTime(): String? =
        preferences.getString(WORKING_TIME, null)

    override fun setWorkingTime(time: String) {
        preferences.put(WORKING_TIME, time)
    }

    companion object {
        const val IS_ADMIN_ENABLED = "IS_ADMIN_ENABLED"
        const val ORGANIZATION_NAME = "ORGANIZATION_NAME"
        const val CONTACT_PHONE_1 = "CONTACT_PHONE_1"
        const val CONTACT_PHONE_2 = "CONTACT_PHONE_2"
        const val CONTACT_EMAIL = "CONTACT_EMAIL"
        const val WEBSITE = "WEBSITE"
        const val VK_GROUP_NAME = "VK_GROUP_NAME"
        const val VK_GROUP_WEBSITE = "VK_GROUP_WEBSITE"
        const val FACEBOOK_NAME = "FACEBOOK_NAME"
        const val FACEBOOK_WEBSITE = "FACEBOOK_WEBSITE"
        const val ADDRESS = "ADDRESS"
        const val WORKING_TIME = "WORKING_TIME"
    }
}