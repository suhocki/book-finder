package app.suhocki.mybooks.ui.catalog

import android.support.v7.widget.RecyclerView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.ads.AdsManager
import app.suhocki.mybooks.data.error.ErrorHandler
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.di.CategoriesDecoration
import app.suhocki.mybooks.di.SearchDecoration
import app.suhocki.mybooks.domain.CatalogInteractor
import app.suhocki.mybooks.domain.model.Header
import app.suhocki.mybooks.domain.model.Search
import app.suhocki.mybooks.ui.base.entity.BookEntity
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.concurrent.Future
import javax.inject.Inject

@InjectViewState
class CatalogPresenter @Inject constructor(
    private val interactor: CatalogInteractor,
    private val resourceManager: ResourceManager,
    private val adsManager: AdsManager,
    private val errorHandler: ErrorHandler,
    private val searchEntity: Search,
    private val header: Header,
    @SearchDecoration private val searchDecoration: RecyclerView.ItemDecoration,
    @CategoriesDecoration private val categoriesDecoration: RecyclerView.ItemDecoration
) : MvpPresenter<CatalogView>(), AnkoLogger {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        doAsync(errorHandler.errorReceiver) {
            val catalogItems = mutableListOf<Any>().apply {
                add(interactor.getBanner())
                add(header)
                addAll(interactor.getCategories())
            }
            uiThread {
                viewState.showCatalogItems(catalogItems, categoriesDecoration)
            }
        }
    }

    override fun attachView(view: CatalogView?) {
        super.attachView(view)
        if (!adsManager.isInterstitialAdLoaded) {
            adsManager.loadInterstitialAd()
        }
    }

    fun startSearchMode(): Future<Unit> {
        return doAsync(errorHandler.errorReceiver) {
            val catalogItems = mutableListOf<Any>().apply {
                add(interactor.getBanner())
                add(searchEntity)
                add(header.apply { title = resourceManager.getString(R.string.enter_query) })
            }
            uiThread {
                viewState.showSearchMode(true)
                viewState.showCatalogItems(
                    catalogItems,
                    scrollToPosition = CatalogFragment.SEARCH_POSITION
                )
            }
        }
    }

    fun stopSearchMode() =
        doAsync(errorHandler.errorReceiver) {
            searchEntity.searchQuery = EMPTY_STRING
            val catalogItems = mutableListOf<Any>().apply {
                add(interactor.getBanner())
                add(header.apply { title = resourceManager.getString(R.string.catalog) })
                addAll(interactor.getCategories())
            }
            uiThread {
                viewState.showSearchMode(false)
                viewState.showCatalogItems(
                    catalogItems,
                    categoriesDecoration,
                    CatalogFragment.BANNER_POSITION
                )
            }
        }

    fun search() = doAsync(errorHandler.errorReceiver) {
        if (searchEntity.searchQuery.isBlank()) return@doAsync
        val catalogItems = mutableListOf<Any>().apply {
            add(interactor.getBanner())
            add(searchEntity)
            add(header.apply { title = resourceManager.getString(R.string.search_results) })
            addAll(interactor.search(searchEntity).apply {
                header.title = resourceManager.getString(
                    if (size > 0) R.string.search_results
                    else R.string.not_found
                )
            })
        }
        uiThread {
            viewState.showCatalogItems(
                catalogItems,
                searchDecoration,
                CatalogFragment.SEARCH_POSITION
            )
        }
    }

    fun clearSearchQuery() = doAsync(errorHandler.errorReceiver) {
        if (searchEntity.searchQuery.isBlank()) stopSearchMode()
        else {
            val catalogItems = mutableListOf<Any>().apply {
                add(interactor.getBanner())
                add(searchEntity)
                add(header.apply { title = resourceManager.getString(R.string.enter_query) })
            }
            searchEntity.searchQuery = EMPTY_STRING
            uiThread {
                viewState.showCatalogItems(
                    catalogItems,
                    scrollToPosition = CatalogFragment.BANNER_POSITION,
                    updateSearchView = true
                )
            }
        }
    }

    fun onSearchQueryChange() {
        viewState.showTopRightButton(!searchEntity.searchQuery.isBlank())
    }

    fun onBuyBookClicked(book: BookEntity) {
        adsManager.setOnAdShownListener {
            adsManager.setOnAdShownListener(null)
            adsManager.loadInterstitialAd()
            viewState.openBookWebsite(book)
            viewState.showBuyDrawableForItem(book, R.drawable.ic_buy)
        }

        when {
            adsManager.isAdShownFor(book.website) -> viewState.openBookWebsite(book)

            adsManager.isInterstitialAdLoading -> {
                viewState.showBuyDrawableForItem(book, R.drawable.ic_time_inverse)
                adsManager.requestShowInterstitialAdFor(book.website)
            }

            else -> adsManager.showInterstitialAd(book.website)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        adsManager.setOnAdShownListener(null)
    }

    companion object {
        const val EMPTY_STRING = ""
    }
}