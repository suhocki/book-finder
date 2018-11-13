package app.suhocki.mybooks.ui.books

import android.arch.persistence.db.SupportSQLiteQuery
import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.ads.AdsManager
import app.suhocki.mybooks.data.service.ServiceHandler
import app.suhocki.mybooks.di.ErrorReceiver
import app.suhocki.mybooks.domain.BooksInteractor
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.ui.base.entity.BookEntity
import app.suhocki.mybooks.ui.firestore.FirestoreService
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject

@InjectViewState
class BooksPresenter @Inject constructor(
    @ErrorReceiver private val errorReceiver: (Throwable) -> Unit,
    private val interactor: BooksInteractor,
    private val category: Category,
    private val adsManager: AdsManager,
    private val serviceHandler: ServiceHandler
) : MvpPresenter<BooksView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.showTitle(category.name)

        serviceHandler.startUpdateService(
            FirestoreService.Command.PULL_BOOKS,
            categoryId = category.id
        )

        loadBooks()
    }

    override fun attachView(view: BooksView?) {
        super.attachView(view)
        adsManager.loadInterstitialAd()
    }

    fun loadBooks(scrollToTop: Boolean = false) {
        viewState.showProgressVisible(true)

        doAsync(errorReceiver) {
            interactor.getBooks(category).let { books ->
                uiThread {
                    if (books.isNotEmpty()) {
                        viewState.showBooks(books, scrollToTop)
                        viewState.showEmptyScreen(false)
                    } else {
                        viewState.showEmptyScreen(true)
                        viewState.showProgressVisible(false)
                    }
                }
            }
        }
    }

    fun setDrawerExpanded(isExpanded: Boolean) {
        viewState.showDrawerExpanded(isExpanded)
    }

    fun applyFilter(sqLiteQuery: SupportSQLiteQuery) = doAsync(errorReceiver) {
        uiThread {
            viewState.showProgressVisible(true)
        }
        val books = interactor.filter(sqLiteQuery)
        uiThread {
            if (books.isNotEmpty()) {
                viewState.showBooks(books, true)
                viewState.showEmptyScreen(false)
            } else {
                viewState.showEmptyScreen(true)
                viewState.showProgressVisible(false)
            }
        }
    }

    fun setProgressInvisible() {
        viewState.showProgressVisible(false)
    }

    fun onBuyBookClicked(book: BookEntity) {
        if (adsManager.isInterstitialAdLoading ||
            adsManager.isInterstitialAdLoaded
        ) {
            adsManager.onAdFlowFinished {
                adsManager.loadInterstitialAd()
                viewState.openBookWebsite(book)
                viewState.showBuyDrawableForItem(book, R.drawable.ic_buy)
            }
        }

        when {
            adsManager.isAdShownFor(book.website) -> viewState.openBookWebsite(book)

            adsManager.isInterstitialAdLoading -> {
                viewState.showBuyDrawableForItem(book, R.drawable.ic_time_inverse)
                adsManager.requestShowInterstitialAdFor(book.website)
            }

            adsManager.isInterstitialAdLoaded -> adsManager.showInterstitialAd(book.website)

            else -> viewState.openBookWebsite(book)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        adsManager.onAdFlowFinished(null)
        serviceHandler.startUpdateService(FirestoreService.Command.CANCEL_PULL_BOOKS)
    }
}