package app.suhocki.mybooks.data.remoteconfig

import app.suhocki.mybooks.BuildConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RemoteConfiguration @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig
) {

    val isAdsEnabled
        get() = remoteConfig.getBoolean(KEY_ADS_ENABLED)

    val isBannerAdEnabled
        get() = remoteConfig.getBoolean(KEY_BANNER_AD_ENABLED)

    val isAboutApplicationEnabled
        get() = BuildConfig.DEBUG || remoteConfig.getBoolean(KEY_ABOUT_APPLICATION_ENABLED)

    private val defaults by lazy {
        mutableMapOf<String, Any>(
            KEY_ADS_ENABLED to false,
            KEY_ABOUT_APPLICATION_ENABLED to false
        )
    }

    init {
        remoteConfig.setConfigSettings(
            FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build()
        )

        remoteConfig.setDefaults(defaults)

        remoteConfig.fetch(getUpdateTime())
            .addOnSuccessListener {
                remoteConfig.activateFetched()
            }
    }

    private fun getUpdateTime() =
        if (BuildConfig.DEBUG) 0
        else TimeUnit.HOURS.toSeconds(UPDATE_TIME_HOURS)

    companion object {
        private const val UPDATE_TIME_HOURS = 12L
        private const val KEY_ADS_ENABLED = "ads_enabled"
        private const val KEY_BANNER_AD_ENABLED = "banner_ad_enabled"
        private const val KEY_ABOUT_APPLICATION_ENABLED = "about_application_enabled"
    }
}