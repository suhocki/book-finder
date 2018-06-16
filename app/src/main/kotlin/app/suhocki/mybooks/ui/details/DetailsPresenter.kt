package app.suhocki.mybooks.ui.details

import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.ads.AdsManager
import app.suhocki.mybooks.domain.model.Book
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import javax.inject.Inject

@InjectViewState
class DetailsPresenter @Inject constructor(
    private var adsManager: AdsManager,
    private var book: Book
) : MvpPresenter<DetailsView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.showFabDrawableRes(R.drawable.ic_buy)
        initAds()
    }

    private fun initAds() {
        if (!adsManager.isInterstitialAdLoaded) {
            adsManager.loadInterstitialAd()
        }
        adsManager.setOnAdShownListener {
            viewState.openBookWebsite(book)
            viewState.showFabDrawableRes(R.drawable.ic_buy)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        adsManager.setOnAdShownListener(null)
    }

    fun onBuyBookClicked() {
        when {
            adsManager.isAdShownFor(book.website) -> viewState.openBookWebsite(book)

            adsManager.isInterstitialAdLoading -> {
                viewState.showFabDrawableRes(R.drawable.ic_time_inverse)
                adsManager.requestShowInterstitialAdFor(book.website)
            }

            else -> adsManager.showInterstitialAd(book.website)
        }
    }
}
