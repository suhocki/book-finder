package suhockii.dev.bookfinder.data.repository

import android.content.SharedPreferences
import suhockii.dev.bookfinder.domain.repository.SettingsRepository
import javax.inject.Inject

class SharedPreferencesRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : SettingsRepository {

    override var databaseLoaded: Boolean
        get() = sharedPreferences.getBoolean(PREFERENCE_IS_DATABASE_LOADED, false)
        set(value) = sharedPreferences.edit().putBoolean(PREFERENCE_IS_DATABASE_LOADED, value).apply()

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

    companion object {
        const val PREFERENCE_IS_DATABASE_LOADED = "PREFERENCE_IS_DATABASE_LOADED"
        const val PREFERENCE_BOOK_COUNT = "PREFERENCE_BOOK_COUNT"
        const val PREFERENCE_CATEGORIES_COUNT = "PREFERENCE_CATEGORIES_COUNT"
    }
}