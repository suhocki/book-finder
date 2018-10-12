package app.suhocki.mybooks.di.provider

import app.suhocki.mybooks.data.context.ContextManager
import net.grandcentrix.tray.AppPreferences
import javax.inject.Inject
import javax.inject.Provider

class AppPreferencesProvider @Inject constructor(
    private val contextManager: ContextManager
) : Provider<AppPreferences> {

    override fun get() =
        AppPreferences(contextManager.currentContext)
}