package app.suhocki.mybooks.data.ads

import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import javax.inject.Inject

class AdsManager @Inject constructor(
    private val interstitialAd: InterstitialAd
) {

    private val adRequest by lazy {
        AdRequest.Builder()
            .addTestDevice("81288C50BEE14137D41BADDDCBC3D173")
            .build()
    }
    private val urlsAdShownFor by lazy { mutableSetOf<String>() }
    private var isWaitingForInterstitialAdLoad = false
    private var onAdFlowFinished: (() -> Unit)? = null
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
                onAdFlowFinished?.invoke()
                onAdFlowFinished = null
            }

            override fun onAdClosed() {
                onAdFlowFinished?.invoke()
                onAdFlowFinished = null
            }
        }
    }

    fun loadInterstitialAd() {
        if (!interstitialAd.isLoaded) {
            interstitialAd.loadAd(adRequest)
        }
    }

    fun isAdShownFor(url: String) =
        urlsAdShownFor.contains(url)

    fun onAdFlowFinished(onAdFlowFinished: (() -> Unit)?) {
        isWaitingForInterstitialAdLoad = false
        this.onAdFlowFinished = onAdFlowFinished
    }

    fun requestShowInterstitialAdFor(url: String) {
        isWaitingForInterstitialAdLoad = true
        urlsAdShownFor.add(url)
    }

    fun showInterstitialAd(url: String) {
        urlsAdShownFor.add(url)
        interstitialAd.show()
    }
}