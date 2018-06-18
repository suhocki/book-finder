package app.suhocki.mybooks.data.ads

import app.suhocki.mybooks.di.provider.RemoteConfigProvider
import app.suhocki.mybooks.domain.model.BannerAd
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import javax.inject.Inject

class AdsManager @Inject constructor(
    private val interstitialAd: InterstitialAd,
    private val bannerAd: BannerAd,
    private val remoteConfig: FirebaseRemoteConfig
) {

    private val adRequest by lazy {
        AdRequest.Builder()
            .addTestDevice(TEST_DEVICE_ID)
            .build()
    }
    private val urlsAdShownFor by lazy { mutableSetOf<String>() }
    private var isWaitingForInterstitialAdLoad = false
    private var onAdFlowFinished: (() -> Unit)? = null
    val isInterstitialAdLoading get() = interstitialAd.isLoading
    val isInterstitialAdLoaded get() = interstitialAd.isLoaded
    val isAdsEnabled get() = remoteConfig.getBoolean(RemoteConfigProvider.KEY_ADS_ENABLED)

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
        if (isAdsEnabled) urlsAdShownFor.contains(url) else true

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

    fun getBannerAd() = bannerAd

    companion object {
        const val TEST_DEVICE_ID = "81288C50BEE14137D41BADDDCBC3D173"
    }
}