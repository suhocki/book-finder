package app.suhocki.mybooks.data.repository

import android.content.SharedPreferences
import app.suhocki.mybooks.domain.repository.InfoRepository
import app.suhocki.mybooks.domain.repository.SettingsRepository
import javax.inject.Inject

class SharedPreferencesRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : SettingsRepository, InfoRepository {

    override var databaseVersion: Int
        get() = sharedPreferences.getInt(PREFERENCE_DATABASE_VERSION, 1)
        set(value) = sharedPreferences.edit().putInt(
            PREFERENCE_DATABASE_VERSION,
            value
        ).apply()

    override var downloadStatistics: Pair<Int, Int>?
        get() = with(sharedPreferences) {
            if (!contains(PREFERENCE_BOOK_COUNT)) return null
            else getInt(PREFERENCE_CATEGORIES_COUNT, -1) to getInt(PREFERENCE_BOOK_COUNT, -1)
        }
        set(value) = with(sharedPreferences) {
            value?.let { (categoriesCount, booksCount) ->
                edit().putInt(PREFERENCE_CATEGORIES_COUNT, categoriesCount)
                    .putInt(PREFERENCE_BOOK_COUNT, booksCount).apply()
            } ?: let {
                edit().remove(PREFERENCE_BOOK_COUNT)
                    .remove(PREFERENCE_CATEGORIES_COUNT).apply()
            }
        }

    override fun getOrganizationName(): String =
        sharedPreferences.getString(
            PREFERENCE_ORGANIZATION_NAME,
            "Книжный интернет-магазин Mybooks.by"
        )

    override fun setOrganizationName(name: String) {
        sharedPreferences.edit().putString(
            PREFERENCE_ORGANIZATION_NAME,
            name
        ).apply()
    }

    override fun getContactPhones(): Set<String> =
        sharedPreferences.getStringSet(
            PREFERENCE_CONTACT_PHONES,
            setOf("375 (29) 696-52-87", "375 (33) 696-52-89")
        )

    override fun setContactPhones(phones: Set<String>) {
        sharedPreferences.edit().putStringSet(
            PREFERENCE_CONTACT_PHONES,
            phones
        ).apply()
    }

    override fun getContactEmail(): String =
        sharedPreferences.getString(
            PREFERENCE_CONTACT_EMAIL,
            "mybooksby@gmail.com"
        )

    override fun setContactEmail(email: String) {
        sharedPreferences.edit().putString(
            PREFERENCE_CONTACT_EMAIL,
            email
        ).apply()
    }

    override fun getWebsite(): Pair<String, String> =
        sharedPreferences.getString(
            PREFERENCE_WEBSITE,
            "mybooks.by"
        ) to "http://mybooks.by"

    override fun setWebsite(website: String) {
        sharedPreferences.edit().putString(
            PREFERENCE_WEBSITE,
            website
        ).apply()
    }

    override fun getVkGroup(): Pair<String, String> =
        sharedPreferences.getString(
            PREFERENCE_VK_GROUP_NAME,
            "Группа Вконтакте"
        ) to sharedPreferences.getString(
            PREFERENCE_VK_GROUP_WEBSITE,
            "https://vk.com/mybooksby"
        )

    override fun setVkGroup(url: String) {
        sharedPreferences.edit().putString(
            PREFERENCE_VK_GROUP_WEBSITE,
            url
        ).apply()
    }

    override fun getFacebook(): Pair<String, String> =
        sharedPreferences.getString(
            PREFERENCE_FACEBOOK_NAME,
            "Группа в Facebook"
        ) to sharedPreferences.getString(
            PREFERENCE_FACEBOOK_WEBSITE,
            "https://www.facebook.com/groups/mybooks.by/"
        )

    override fun setFacebook(url: String) {
        sharedPreferences.edit().putString(
            PREFERENCE_FACEBOOK_WEBSITE,
            url
        ).apply()
    }

    override fun getAddress(): String =
        sharedPreferences.getString(
            PREFERENCE_ADDRESS,
            "Минск, ТЦ Купаловский, пав. 7, (м. Октябрьская)"
        )

    override fun setAddress(address: String) {
        sharedPreferences.edit().putString(
            PREFERENCE_ADDRESS,
            address
        ).apply()
    }

    override fun getWorkingTime(): String =
        sharedPreferences.getString(
            PREFERENCE_WORKING_TIME,
            "ПН-СБ. 11.00 -19.00"
        )

    override fun setWorkingTime(time: String) {
        sharedPreferences.edit().putString(
            PREFERENCE_WORKING_TIME,
            time
        ).apply()
    }

    companion object {
        const val PREFERENCE_DATABASE_VERSION = "PREFERENCE_DATABASE_VERSION"
        const val PREFERENCE_BOOK_COUNT = "PREFERENCE_BOOK_COUNT"
        const val PREFERENCE_CATEGORIES_COUNT = "PREFERENCE_CATEGORIES_COUNT"
        const val PREFERENCE_ORGANIZATION_NAME = "PREFERENCE_ORGANIZATION_NAME"
        const val PREFERENCE_CONTACT_PHONES = "PREFERENCE_CONTACT_PHONES"
        const val PREFERENCE_CONTACT_EMAIL = "PREFERENCE_CONTACT_EMAIL"
        const val PREFERENCE_WEBSITE = "PREFERENCE_WEBSITE"
        const val PREFERENCE_VK_GROUP_NAME = "PREFERENCE_VK_GROUP_NAME"
        const val PREFERENCE_VK_GROUP_WEBSITE = "PREFERENCE_VK_GROUP_WEBSITE"
        const val PREFERENCE_FACEBOOK_NAME = "PREFERENCE_FACEBOOK_NAME"
        const val PREFERENCE_FACEBOOK_WEBSITE = "PREFERENCE_FACEBOOK_WEBSITE"
        const val PREFERENCE_ADDRESS = "PREFERENCE_ADDRESS"
        const val PREFERENCE_WORKING_TIME = "PREFERENCE_WORKING_TIME"
    }
}