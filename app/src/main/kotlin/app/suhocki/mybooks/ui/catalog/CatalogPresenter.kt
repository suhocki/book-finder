package app.suhocki.mybooks.ui.catalog

import android.content.res.Resources
import android.support.v7.widget.RecyclerView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.ads.AdsManager
import app.suhocki.mybooks.data.firestore.FirestoreObserver
import app.suhocki.mybooks.data.firestore.entity.FirestoreCategory
import app.suhocki.mybooks.data.mapper.Mapper
import app.suhocki.mybooks.data.remoteconfig.RemoteConfiguration
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.data.room.entity.BookDbo
import app.suhocki.mybooks.di.*
import app.suhocki.mybooks.domain.ListTools
import app.suhocki.mybooks.domain.model.Search
import app.suhocki.mybooks.domain.model.SearchResult
import app.suhocki.mybooks.domain.repository.BannersRepository
import app.suhocki.mybooks.domain.repository.BooksRepository
import app.suhocki.mybooks.presentation.base.paginator.PaginationView
import app.suhocki.mybooks.presentation.base.paginator.Paginator
import app.suhocki.mybooks.ui.base.entity.PageProgress
import app.suhocki.mybooks.ui.base.entity.UiItem
import app.suhocki.mybooks.ui.catalog.entity.UiCategory
import app.suhocki.mybooks.uiThread
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.google.firebase.firestore.DocumentChange
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import java.util.concurrent.Future
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@InjectViewState
class CatalogPresenter @Inject constructor(
    @IsSearchMode private val isSearchMode: AtomicBoolean,
    @ErrorReceiver private val errorReceiver: (Throwable) -> Unit,
    @SearchAll private val searchEntity: Search,

    @SearchDecoration private val searchDecoration: RecyclerView.ItemDecoration,
    @CategoriesDecoration private val categoriesDecoration: RecyclerView.ItemDecoration,

    @Room private val booksRepository: BooksRepository,
    @Room private val bannersRepository: BannersRepository,

    private val firestoreObserver: FirestoreObserver,
    private val resourceManager: ResourceManager,
    private val adsManager: AdsManager,
    private val remoteConfigurator: RemoteConfiguration,
    private val allData: MutableList<UiItem>,
    private val listTools: ListTools,
    private val mapper: Mapper
) : MvpPresenter<CatalogView>(), AnkoLogger {

    private val categoriesFactory: (Int) -> List<UiCategory> = { page: Int ->
        val snapshot = firestoreObserver.observePage(
            page.dec() * Paginator.LIMIT,
            Paginator.LIMIT
        )
        mapper.map(snapshot, UiCategory::class.java)
    }

    private val paginationView = object : PaginationView<UiItem> {
        override fun showEmptyProgress(show: Boolean) {
            viewState.showEmptyProgress(show)
        }

        override fun showEmptyError(show: Boolean, error: Throwable?) {
            error?.let {
                viewState.showEmptyError(show, error)
            } ?: viewState.showEmptyError(show, null)
        }

        override fun showErrorMessage(error: Throwable) {
            viewState.showErrorMessage(error)
        }

        override fun showEmptyView(show: Boolean) {
            viewState.showEmptyView(show)
        }

        override fun showData(data: List<UiItem>) {
            val mutableData = data.toMutableList()
            listTools.addProgressAndSetTrigger(mutableData, Paginator.LIMIT)
            viewState.showData(mutableData)
        }

        override fun showRefreshProgress(show: Boolean) {
            viewState.showRefreshProgress(show)
        }

        override fun hidePageProgress() {
            viewState.hidePageProgress()
        }
    }

    private val paginator = Paginator(firestoreObserver, paginationView, allData, categoriesFactory)


    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadData()

        firestoreObserver.addOnNewSnapshotListener onNewSnapshot@{ snapshot, offset, limit ->
            val pageData: List<UiCategory> = mapper.map(snapshot, UiCategory::class.java)

            if (pageData.isEmpty() && offset == 0) {
                allData.clear()
            }

            listTools.updatePageData(allData, pageData, offset, Paginator.LIMIT)

            val hasRemovedOrAddedItems =
                snapshot.documentChanges.any {
                    it.type == DocumentChange.Type.ADDED ||
                            it.type == DocumentChange.Type.REMOVED
                }

            if (hasRemovedOrAddedItems) {
                val disposedCount = firestoreObserver.dispose((offset + limit) / limit)

                when {
                    pageData.size < limit && disposedCount == 0 || pageData.isEmpty() ->
                        allData.removeAll { it is PageProgress }

                    pageData.size == limit && disposedCount == 0 ->
                        listTools.addProgressAndSetTrigger(allData, Paginator.LIMIT)

                    pageData.size == limit -> {
                        val nextPageOffset = offset + limit
                        allData.subList(nextPageOffset, allData.size).clear()
                        firestoreObserver.removeSnapshots(nextPageOffset)

                        doAsync {
                            val querySnapshots = firestoreObserver
                                .getResubscribedData(disposedCount, nextPageOffset, limit)

                            val categories = querySnapshots
                                .flatMap { it.toObjects(FirestoreCategory::class.java) }
                                .mapTo(mutableListOf<UiCategory>()) { mapper.map(it) }

                            allData.addAll(categories)
                            listTools.addProgressAndSetTrigger(allData, Paginator.LIMIT)
                            uiThread { viewState.showData(allData) }
                        }
                        return@onNewSnapshot
                    }
                }
            }

            uiThread { viewState.showData(allData) }

            firestoreObserver.sendObserversCount()
        }
    }

    fun loadData() {
        if (isSearchMode.get()) return

        paginator.refresh()
    }

    fun loadNextPage() {
        paginator.loadNewPage()
    }

    override fun attachView(view: CatalogView?) {
        super.attachView(view)
        adsManager.loadInterstitialAd()
    }

    fun startSearchMode(): Future<Unit> {
        return doAsync(errorReceiver) {
            //            isSearchMode.set(true)
//            val catalogItems = mutableListOf<UiItem>().apply {
//                getBanner()?.let { add(it) }
//                add(searchEntity)
//                add(HeaderEntity(resourceManager.getString(R.string.enter_query)))
//            }
//            uiThread {
//                viewState.showSearchMode(true)
//                viewState.showCatalogItems(
//                    catalogItems,
//                    scrollToPosition = CatalogFragment.SEARCH_POSITION
//                )
//            }
        }
    }

    fun stopSearchMode() =
        doAsync(errorReceiver) {
            //            isSearchMode.set(false)
//            searchEntity.searchQuery = EMPTY_STRING
//            val catalogItems = mutableListOf<Any>().apply {
//                getBanner()?.let { add(it) }
//                add(HeaderEntity(resourceManager.getString(R.string.catalog)))
//                addAll(getCategories())
//            }
//            uiThread {
//                viewState.showSearchMode(false)
//                viewState.showCatalogItems(
//                    catalogItems,
//                    categoriesDecoration,
//                    CatalogFragment.BANNER_POSITION
//                )
//            }
        }

    fun removeScrollCommand(
        catalogItems: List<UiItem>,
        itemDecoration: RecyclerView.ItemDecoration?
    ) {
//        viewState.showCatalogItems(
//            catalogItems,
//            itemDecoration,
//            CatalogFragment.UNDEFINED_POSITION
//        )
    }

    fun search() = doAsync(errorReceiver) {
        //        if (searchEntity.searchQuery.isBlank()) return@doAsync
//        val catalogItems = mutableListOf<Any>().apply {
//            getBanner()?.let { add(it) }
//            add(searchEntity)
//            val searchResults = search(searchEntity)
//            val title = resourceManager.getString(
//                if (searchResults.isNotEmpty()) R.string.search_results
//                else R.string.not_found
//            )
//            add(HeaderEntity(title))
//            addAll(searchResults)
//        }
//        uiThread {
//            viewState.showCatalogItems(
//                catalogItems,
//                searchDecoration,
//                CatalogFragment.SEARCH_POSITION
//            )
//        }
    }

    fun clearSearchQuery() = doAsync(errorReceiver) {
        //        if (searchEntity.searchQuery.isBlank()) stopSearchMode()
//        else {
//            val catalogItems = mutableListOf<Any>().apply {
//                getBanner()?.let { add(it) }
//                add(searchEntity)
//                add(HeaderEntity(resourceManager.getString(R.string.enter_query)))
//            }
//            searchEntity.searchQuery = EMPTY_STRING
//            uiThread {
//                viewState.showCatalogItems(
//                    catalogItems,
//                    scrollToPosition = CatalogFragment.BANNER_POSITION,
//                    updateSearchView = true
//                )
//            }
//        }
    }

    fun onSearchQueryChange() {
        viewState.showTopRightButton(!searchEntity.searchQuery.isBlank())
    }

    fun onBuyBookClicked(book: app.suhocki.mybooks.ui.base.entity.UiBook) {
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

    private fun getBanner(): Any? =
        if (remoteConfigurator.isBannerAdEnabled) adsManager.getBannerAd()
        else bannersRepository.getBanners().firstOrNull()

    private fun search(search: Search) =
        booksRepository.search(search.searchQuery)
            .map {
                object : SearchResult {
                    override val foundBy = determineFoundBy(search, it)
                    override val book = it
                }
            }
            .toList<SearchResult>()

    private fun determineFoundBy(
        search: Search,
        book: BookDbo
    ): String {
        val q = search.searchQuery
        return when {
            book.id.contains(q, true) ->
                "${resourceManager.getString(R.string.isbn)}: ${book.id}"

            book.fullName.contains(q, true) ->
                "${resourceManager.getString(R.string.category)}: ${book.categoryId}"

            book.shortName.contains(q, true) ->
                "${resourceManager.getString(R.string.category)}: ${book.categoryId}"

            book.author?.contains(q, true) ?: false ->
                "${resourceManager.getString(R.string.author)}: ${book.author}"

            book.publisher?.contains(q, true) ?: false ->
                "${resourceManager.getString(R.string.publisher)}: ${book.publisher}"

            book.year?.contains(q, true) ?: false ->
                "${resourceManager.getString(R.string.year)}: ${book.year}"

            else -> throw Resources.NotFoundException()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        adsManager.onAdFlowFinished()
        paginator.release()
        firestoreObserver.dispose()
    }

    fun removePageProgress(items: List<UiItem>) {
        val changedItems = items.toMutableList()

        if (items.lastOrNull() is PageProgress) changedItems.remove(items.last())

        viewState.showData(changedItems)
    }
}