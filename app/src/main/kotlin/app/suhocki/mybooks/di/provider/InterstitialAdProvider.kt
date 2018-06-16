package app.suhocki.mybooks.di.provider

import android.content.Context
import app.suhocki.mybooks.inDebug
import app.suhocki.mybooks.inRelease
import com.google.android.gms.ads.InterstitialAd
import javax.inject.Inject
import javax.inject.Provider



class InterstitialAdProvider @Inject constructor(
    private val context: Context
): Provider<InterstitialAd> {

    override fun get(): InterstitialAd =
        InterstitialAd(context).apply {
            inDebug { adUnitId = "ca-app-pub-3940256099942544/1033173712" }
            inRelease { adUnitId = "ca-app-pub-5580850164009775/9559469093" }
        }
}