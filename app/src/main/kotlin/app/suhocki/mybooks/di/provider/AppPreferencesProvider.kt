package app.suhocki.mybooks.di.provider

import android.content.Context
import net.grandcentrix.tray.AppPreferences
import javax.inject.Inject
import javax.inject.Provider

class AppPreferencesProvider @Inject constructor(
    private val context: Context
) : Provider<AppPreferences> {

    override fun get() =
        AppPreferences(context)
}