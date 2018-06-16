package app.suhocki.mybooks.data.ads

import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import javax.inject.Inject

class AdsManager @Inject constructor(
    private val interstitialAd: InterstitialAd
) {

    private val adRequest by lazy { AdRequest.Builder().build() }

    private var isWaitingForInterstitialAdLoad = false

    val isLoadingInterstitialAd
        get() = interstitialAd.isLoading

    fun initInterstitialAd(
        onLoadFailed: () -> Unit,
        onAdClosed: () -> Unit
    ) {
        isWaitingForInterstitialAdLoad = false
        interstitialAd.adListener = object : AdListener() {
            override fun onAdLoaded() {
                if (isWaitingForInterstitialAdLoad) {
                    interstitialAd.show()
                }
            }

            override fun onAdFailedToLoad(p0: Int) {
                onLoadFailed.invoke()
            }

            override fun onAdClosed() {
                onAdClosed.invoke()
            }
        }
        interstitialAd.loadAd(adRequest)
    }

    fun requestShowInterstitialAd() {
        isWaitingForInterstitialAdLoad = true
    }

    fun showInterstitialAd() {
        interstitialAd.show()
    }
}