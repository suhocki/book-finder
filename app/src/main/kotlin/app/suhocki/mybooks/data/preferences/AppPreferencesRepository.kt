package app.suhocki.mybooks.data.preferences

import app.suhocki.mybooks.domain.repository.SettingsRepository
import net.grandcentrix.tray.AppPreferences
import javax.inject.Inject

class AppPreferencesRepository @Inject constructor(
    private val preferences: AppPreferences
) : SettingsRepository {

    override var isAdminModeEnabled: Boolean
        get() = preferences.getBoolean(IS_ADMIN_ENABLED, false)
        set(value) {
            preferences.put(IS_ADMIN_ENABLED, value)
        }

    companion object {
        const val IS_ADMIN_ENABLED = "IS_ADMIN_ENABLED"
    }
}