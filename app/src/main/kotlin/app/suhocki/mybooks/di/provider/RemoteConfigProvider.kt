package app.suhocki.mybooks.di.provider

import app.suhocki.mybooks.BuildConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider

class RemoteConfigProvider @Inject constructor() : Provider<FirebaseRemoteConfig> {

    override fun get(): FirebaseRemoteConfig =
        FirebaseRemoteConfig.getInstance().apply {
            val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build()
            setConfigSettings(configSettings)

            val defaults = mutableMapOf<String, Any>(
                KEY_ADS_ENABLED to false
            )

            setDefaults(defaults)

            val fetch = fetch(if (BuildConfig.DEBUG) 0 else TimeUnit.HOURS.toSeconds(12))
            fetch.addOnSuccessListener { activateFetched() }
        }

    companion object {
        const val KEY_ADS_ENABLED = "ads_enabled"
    }
}