package app.suhocki.mybooks.data.repository

import android.content.SharedPreferences
import app.suhocki.mybooks.domain.repository.InfoRepository
import app.suhocki.mybooks.domain.repository.SettingsRepository
import javax.inject.Inject

class SharedPreferencesRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : SettingsRepository, InfoRepository {

    override var databaseLoaded: Boolean
        get() = sharedPreferences.getBoolean(PREFERENCE_IS_DATABASE_LOADED, false)
        set(value) = sharedPreferences.edit().putBoolean(
            PREFERENCE_IS_DATABASE_LOADED,
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

    override fun getContactPhones(): Set<String> =
        sharedPreferences.getStringSet(
            PREFERENCE_CONTACT_PHONES,
            setOf("375 (29) 696-52-87", "375 (33) 696-52-89")
        )

    override fun getContactEmail(): String =
        sharedPreferences.getString(
            PREFERENCE_CONTACT_EMAIL,
            "mybooksby@gmail.com"
        )

    override fun getWebsite(): String =
        sharedPreferences.getString(
            PREFERENCE_WEBSITE,
            "mybooks.by"
        )

    override fun getVkGroup(): Pair<String, String> =
        sharedPreferences.getString(
            PREFERENCE_VK_GROUP_NAME,
            "Группа Вконтакте"
        ) to sharedPreferences.getString(
            PREFERENCE_VK_GROUP_WEBSITE,
            "https://vk.com/mybooksby"
        )

    override fun getFacebook(): Pair<String, String> =
        sharedPreferences.getString(
            PREFERENCE_FACEBOOK_NAME,
            "Группа в Facebook"
        ) to sharedPreferences.getString(
            PREFERENCE_FACEBOOK_WEBSITE,
            "https://www.facebook.com/groups/mybooks.by/"
        )


    companion object {
        const val PREFERENCE_IS_DATABASE_LOADED = "PREFERENCE_IS_DATABASE_LOADED"
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
    }
}