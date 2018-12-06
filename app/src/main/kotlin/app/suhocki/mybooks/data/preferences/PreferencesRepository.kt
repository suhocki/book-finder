package app.suhocki.mybooks.data.preferences

import net.grandcentrix.tray.AppPreferences
import javax.inject.Inject

class PreferencesRepository @Inject constructor(
    private val preferences: AppPreferences
) {

    var isAdminModeEnabled: Boolean
        get() = preferences.getBoolean(IS_ADMIN_ENABLED, false)
        set(value) {
            preferences.put(IS_ADMIN_ENABLED, value)
        }

    var isDebugPanelEnabled: Boolean
        get() = preferences.getBoolean(IS_DEBUG_PANEL_ENABLED, false)
        set(value) {
            preferences.put(IS_DEBUG_PANEL_ENABLED, value)
        }

    var updateDate: String
        get() = preferences.getString(UPDATE_DATE, "")!!
        set(value) {
            preferences.put(UPDATE_DATE, value)
        }

    companion object {
        const val IS_ADMIN_ENABLED = "IS_ADMIN_ENABLED"
        const val IS_DEBUG_PANEL_ENABLED = "IS_DEBUG_PANEL_ENABLED"
        const val UPDATE_DATE = "UPDATE_DATE"
    }
}