package app.suhocki.mybooks.di.provider

import android.content.Context
import android.content.SharedPreferences
import app.suhocki.mybooks.BuildConfig
import app.suhocki.mybooks.data.context.ContextManager
import javax.inject.Inject
import javax.inject.Provider

class SharedPreferencesProvider @Inject constructor(
    private val contextManager: ContextManager
) : Provider<SharedPreferences> {

    override fun get(): SharedPreferences =
        contextManager.currentContext.getSharedPreferences(
            BuildConfig.SHARED_PREFERENCES_FILE_NAME,
            Context.MODE_PRIVATE
        )
}