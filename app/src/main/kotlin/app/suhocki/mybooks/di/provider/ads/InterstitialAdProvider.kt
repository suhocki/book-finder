package app.suhocki.mybooks.di.provider.ads

import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.context.ContextManager
import com.google.android.gms.ads.InterstitialAd
import javax.inject.Inject
import javax.inject.Provider


class InterstitialAdProvider @Inject constructor(
    private val contextManager: ContextManager
): Provider<InterstitialAd> {

    override fun get(): InterstitialAd =
        InterstitialAd(contextManager.applicationContext).apply {
            adUnitId = contextManager.currentContext.getString(R.string.interstitial_ad_id)
        }
}