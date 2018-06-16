package app.suhocki.mybooks.ui.details

import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.ads.AdsManager
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import javax.inject.Inject

@InjectViewState
class DetailsPresenter @Inject constructor(
    private var adsManager: AdsManager
) : MvpPresenter<DetailsView>() {

    private var isBookWebsiteAlreadyOpened = false

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.showFabDrawableRes(R.drawable.ic_buy)
        initAds()
    }

    private fun initAds() {
        adsManager.initInterstitialAd(onLoadFailed = {
            isBookWebsiteAlreadyOpened = true
            viewState.openBookWebsite()
            viewState.showFabDrawableRes(R.drawable.ic_buy)
        }, onAdClosed = {
            isBookWebsiteAlreadyOpened = true
            viewState.openBookWebsite()
            viewState.showFabDrawableRes(R.drawable.ic_buy)
        })
    }

    fun onBuyClicked() {
        when {
            isBookWebsiteAlreadyOpened -> viewState.openBookWebsite()

            adsManager.isLoadingInterstitialAd -> {
                viewState.showFabDrawableRes(R.drawable.ic_time_inverse)
                adsManager.requestShowInterstitialAd()
            }

            else -> adsManager.showInterstitialAd()
        }
    }
}
