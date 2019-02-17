package app.suhocki.mybooks.data.remoteconfig

import app.suhocki.mybooks.BuildConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RemoteConfig @Inject constructor(
    private val firebaseRemoteConfig: FirebaseRemoteConfig
) {

    val isAdsEnabled
        get() = firebaseRemoteConfig.getBoolean(KEY_ADS_ENABLED)

    val isBannerAdEnabled
        get() = firebaseRemoteConfig.getBoolean(KEY_BANNER_AD_ENABLED)

    val isAboutApplicationEnabled
        get() = firebaseRemoteConfig.getBoolean(KEY_ABOUT_APPLICATION_ENABLED)

    private val defaults by lazy {
        mutableMapOf<String, Any>(
            KEY_ADS_ENABLED to false,
            KEY_BANNER_AD_ENABLED to false,
            KEY_ABOUT_APPLICATION_ENABLED to true
        )
    }

    init {
        firebaseRemoteConfig.setConfigSettings(
            FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build()
        )

        firebaseRemoteConfig.setDefaults(defaults)

        firebaseRemoteConfig.fetch(getUpdateTime())
            .addOnSuccessListener {
                firebaseRemoteConfig.activateFetched()
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