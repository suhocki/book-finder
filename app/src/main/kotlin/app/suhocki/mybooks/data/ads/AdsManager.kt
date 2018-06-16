package app.suhocki.mybooks.data.ads

import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import javax.inject.Inject

class AdsManager @Inject constructor(
    private val interstitialAd: InterstitialAd
) {

    private val adRequest by lazy { AdRequest.Builder().build() }
    private val urlsAdShownFor by lazy { mutableSetOf<String>() }
    private var isWaitingForInterstitialAdLoad = false
    private var onAdShown: (() -> Unit)? = null
    val isInterstitialAdLoading get() = interstitialAd.isLoading
    val isInterstitialAdLoaded get() = interstitialAd.isLoaded

    init {
        interstitialAd.adListener = object : AdListener() {
            override fun onAdLoaded() {
                if (isWaitingForInterstitialAdLoad) {
                    interstitialAd.show()
                }
            }

            override fun onAdFailedToLoad(p0: Int) {
                onAdShown?.invoke()
            }

            override fun onAdClosed() {
                onAdShown?.invoke()
            }
        }
    }

    fun loadInterstitialAd() =
        interstitialAd.loadAd(adRequest)

    fun isAdShownFor(url: String) =
        urlsAdShownFor.contains(url)

    fun setOnAdShownListener(onAdShown: (() -> Unit)?) {
        isWaitingForInterstitialAdLoad = false
        this.onAdShown = onAdShown
    }

    fun requestShowInterstitialAdFor(url: String) {
        urlsAdShownFor.add(url)
        isWaitingForInterstitialAdLoad = true
    }

    fun showInterstitialAd(url: String) {
        urlsAdShownFor.add(url)
        interstitialAd.show()
    }
}