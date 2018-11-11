package app.suhocki.mybooks.ui.catalog

import android.support.v7.widget.RecyclerView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.ads.AdsManager
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.data.service.ServiceHandler
import app.suhocki.mybooks.di.*
import app.suhocki.mybooks.domain.CatalogInteractor
import app.suhocki.mybooks.domain.model.Search
import app.suhocki.mybooks.ui.base.entity.BookEntity
import app.suhocki.mybooks.ui.catalog.entity.HeaderEntity
import app.suhocki.mybooks.ui.firestore.FirestoreService
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.concurrent.Future
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@InjectViewState
class CatalogPresenter @Inject constructor(
    @IsSearchMode private val isSearchMode: AtomicBoolean,
    @ErrorReceiver private val errorReceiver: (Throwable) -> Unit,
    private val interactor: CatalogInteractor,
    private val resourceManager: ResourceManager,
    private val adsManager: AdsManager,
    @SearchAll private val searchEntity: Search,
    @SearchDecoration private val searchDecoration: RecyclerView.ItemDecoration,
    @CategoriesDecoration private val categoriesDecoration: RecyclerView.ItemDecoration,
    private val serviceHandler: ServiceHandler
) : MvpPresenter<CatalogView>(), AnkoLogger {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        serviceHandler.startUpdateService(FirestoreService.Command.PULL_CATEGORIES)
        loadData()
    }

    fun loadData() {
        if (isSearchMode.get()) return

        doAsync(errorReceiver) {
            val catalogItems = mutableListOf<Any>().apply {
                interactor.getBanner()?.let { add(it) }
                add(HeaderEntity(title = resourceManager.getString(R.string.catalog)))
                addAll(interactor.getCategories())
            }
            uiThread {
                viewState.showCatalogItems(catalogItems, categoriesDecoration)
            }
        }
    }

    override fun attachView(view: CatalogView?) {
        super.attachView(view)
        adsManager.loadInterstitialAd()
    }

    fun startSearchMode(): Future<Unit> {
        return doAsync(errorReceiver) {
            isSearchMode.set(true)
            val catalogItems = mutableListOf<Any>().apply {
                interactor.getBanner()?.let { add(it) }
                add(searchEntity)
                add(HeaderEntity(title = resourceManager.getString(R.string.enter_query)))
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
        doAsync(errorReceiver) {
            isSearchMode.set(false)
            searchEntity.searchQuery = EMPTY_STRING
            val catalogItems = mutableListOf<Any>().apply {
                interactor.getBanner()?.let { add(it) }
                add(HeaderEntity(title = resourceManager.getString(R.string.catalog)))
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

    fun removeScrollCommand(
        catalogItems: List<Any>,
        itemDecoration: RecyclerView.ItemDecoration?
    ) {
        viewState.showCatalogItems(
            catalogItems,
            itemDecoration,
            CatalogFragment.UNDEFINED_POSITION
        )
    }

    fun search() = doAsync(errorReceiver) {
        if (searchEntity.searchQuery.isBlank()) return@doAsync
        val catalogItems = mutableListOf<Any>().apply {
            interactor.getBanner()?.let { add(it) }
            add(searchEntity)
            val searchResults = interactor.search(searchEntity)
            val title = resourceManager.getString(
                if (searchResults.isNotEmpty()) R.string.search_results
                else R.string.not_found
            )
            add(HeaderEntity(title = title))
            addAll(searchResults)
        }
        uiThread {
            viewState.showCatalogItems(
                catalogItems,
                searchDecoration,
                CatalogFragment.SEARCH_POSITION
            )
        }
    }

    fun clearSearchQuery() = doAsync(errorReceiver) {
        if (searchEntity.searchQuery.isBlank()) stopSearchMode()
        else {
            val catalogItems = mutableListOf<Any>().apply {
                interactor.getBanner()?.let { add(it) }
                add(searchEntity)
                add(HeaderEntity(title = resourceManager.getString(R.string.enter_query)))
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
    }

    companion object {
        const val EMPTY_STRING = ""
    }
}