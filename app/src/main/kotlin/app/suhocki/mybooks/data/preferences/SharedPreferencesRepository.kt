package app.suhocki.mybooks.data.preferences

import android.content.SharedPreferences
import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.domain.repository.InfoRepository
import app.suhocki.mybooks.domain.repository.SettingsRepository
import javax.inject.Inject

class SharedPreferencesRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val resourceManager: ResourceManager
) : SettingsRepository, InfoRepository {

    override var databaseVersion: Int
        get() = sharedPreferences.getInt(DATABASE_VERSION, 1)
        set(value) = sharedPreferences.edit().putInt(DATABASE_VERSION, value).apply()

    override var downloadStatistics: Pair<Int, Int>?
        get() = with(sharedPreferences) {
            if (!contains(BOOK_COUNT)) return null
            else getInt(CATEGORIES_COUNT, -1) to getInt(
                BOOK_COUNT, -1
            )
        }
        set(value) = with(sharedPreferences) {
            value?.let { (categoriesCount, booksCount) ->
                edit().putInt(CATEGORIES_COUNT, categoriesCount)
                    .putInt(BOOK_COUNT, booksCount).apply()
            } ?: let {
                edit().remove(BOOK_COUNT)
                    .remove(CATEGORIES_COUNT).apply()
            }
        }

    override var isAdminModeEnabled: Boolean
        get() = sharedPreferences.getBoolean(IS_ADMIN_ENABLED, false)
        set(value) = sharedPreferences.edit().putBoolean(IS_ADMIN_ENABLED, value).apply()

    override fun getOrganizationName(): String? =
        sharedPreferences.getString(ORGANIZATION_NAME, null)

    override fun setOrganizationName(name: String) {
        sharedPreferences.edit().putString(
            ORGANIZATION_NAME,
            name
        ).apply()
    }

    override fun getContactPhones(): Set<String>? =
        sharedPreferences.getStringSet(CONTACT_PHONES, null)

    override fun setContactPhones(phones: Set<String>) {
        sharedPreferences.edit().putStringSet(CONTACT_PHONES, phones).apply()
    }

    override fun getContactEmail(): String? =
        sharedPreferences.getString(CONTACT_EMAIL, null)

    override fun setContactEmail(email: String) {
        sharedPreferences.edit().putString(CONTACT_EMAIL, email).apply()
    }

    override fun getWebsite(): Pair<String, String>? = with(sharedPreferences) {
        if (contains(WEBSITE)) sharedPreferences.getString(WEBSITE, null) to
                resourceManager.getString(R.string.website_url)
        else null
    }

    override fun setWebsite(website: String) {
        sharedPreferences.edit().putString(WEBSITE, website).apply()
    }

    override fun getVkGroup(): Pair<String, String>? = with(sharedPreferences) {
        if (contains(VK_GROUP_NAME) && contains(VK_GROUP_WEBSITE))
            getString(VK_GROUP_NAME, null) to getString(VK_GROUP_WEBSITE, null)
        else null
    }

    override fun setVkGroup(url: String) {
        sharedPreferences.edit().putString(VK_GROUP_WEBSITE, url).apply()
    }

    override fun getFacebook(): Pair<String, String>? = with(sharedPreferences) {
        if (contains(FACEBOOK_NAME) && contains(FACEBOOK_WEBSITE))
            getString(FACEBOOK_NAME, null) to getString(FACEBOOK_WEBSITE, null)
        else null
    }

    override fun setFacebook(url: String) {
        sharedPreferences.edit().putString(FACEBOOK_WEBSITE, url).apply()
    }

    override fun getAddress(): String? =
        sharedPreferences.getString(ADDRESS, null)

    override fun setAddress(address: String) {
        sharedPreferences.edit().putString(ADDRESS, address).apply()
    }

    override fun getWorkingTime(): String? =
        sharedPreferences.getString(WORKING_TIME, null)

    override fun setWorkingTime(time: String) {
        sharedPreferences.edit().putString(WORKING_TIME, time).apply()
    }

    companion object {
        const val DATABASE_VERSION = "DATABASE_VERSION"
        const val IS_ADMIN_ENABLED = "IS_ADMIN_ENABLED"
        const val BOOK_COUNT = "BOOK_COUNT"
        const val CATEGORIES_COUNT = "CATEGORIES_COUNT"
        const val ORGANIZATION_NAME = "ORGANIZATION_NAME"
        const val CONTACT_PHONES = "CONTACT_PHONES"
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