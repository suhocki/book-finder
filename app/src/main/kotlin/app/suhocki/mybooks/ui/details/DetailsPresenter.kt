package app.suhocki.mybooks.ui.details

import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.ads.AdsManager
import app.suhocki.mybooks.di.BookId
import app.suhocki.mybooks.di.Room
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.domain.repository.BooksRepository
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject

@InjectViewState
class DetailsPresenter @Inject constructor(
    private val adsManager: AdsManager,
    @Room private val booksRepository: BooksRepository,
    @BookId private val bookId: String
) : MvpPresenter<DetailsView>() {

    private lateinit var book: Book

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadBook()
        adsManager.loadInterstitialAd()

    }

    private fun loadBook() {
        doAsync {
            val book = booksRepository.getBookById(bookId)
            uiThread {
                viewState.showBook(book)
                viewState.showFabDrawableRes(R.drawable.ic_buy)
            }
        }
    }

    fun onBuyBookClicked() {
        if (adsManager.isInterstitialAdLoading ||
            adsManager.isInterstitialAdLoaded
        ) {
            adsManager.onAdFlowFinished {
                adsManager.loadInterstitialAd()
                viewState.openBookWebsite(book)
                viewState.showFabDrawableRes(R.drawable.ic_buy)
            }
        }

        when {
            adsManager.isAdShownFor(book.website) -> viewState.openBookWebsite(book)

            adsManager.isInterstitialAdLoading -> {
                viewState.showFabDrawableRes(R.drawable.ic_time_inverse)
                adsManager.requestShowInterstitialAdFor(book.website)
            }

            adsManager.isInterstitialAdLoaded -> adsManager.showInterstitialAd(book.website)

            else -> viewState.openBookWebsite(book)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        adsManager.onAdFlowFinished()
    }
}
