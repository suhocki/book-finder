package app.suhocki.mybooks.di.provider

import android.content.Context
import com.google.android.gms.ads.InterstitialAd
import javax.inject.Inject
import javax.inject.Provider



class InterstitialAdProvider @Inject constructor(
    private val context: Context
): Provider<InterstitialAd> {

    override fun get(): InterstitialAd =
        InterstitialAd(context).apply {
            adUnitId = "ca-app-pub-3940256099942544/1033173712"
        }
}