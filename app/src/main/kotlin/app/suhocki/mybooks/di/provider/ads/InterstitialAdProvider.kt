package app.suhocki.mybooks.di.provider.ads

import android.content.Context
import app.suhocki.mybooks.R
import com.google.android.gms.ads.InterstitialAd
import javax.inject.Inject
import javax.inject.Provider



class InterstitialAdProvider @Inject constructor(
    private val context: Context
): Provider<InterstitialAd> {

    override fun get(): InterstitialAd =
        InterstitialAd(context).apply {
            adUnitId = context.getString(R.string.interstitial_ad_id)
        }
}