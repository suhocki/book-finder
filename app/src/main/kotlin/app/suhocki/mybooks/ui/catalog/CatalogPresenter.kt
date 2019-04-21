package app.suhocki.mybooks.ui.catalog

import app.suhocki.mybooks.R
import app.suhocki.mybooks.Screens
import app.suhocki.mybooks.data.firestore.FirestoreObserver
import app.suhocki.mybooks.data.mapper.Mapper
import app.suhocki.mybooks.di.BannersObserver
import app.suhocki.mybooks.di.CategoriesObserver
import app.suhocki.mybooks.domain.model.Banner
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.model.system.flow.FlowRouter
import app.suhocki.mybooks.presentation.global.paginator.FirestorePaginator
import app.suhocki.mybooks.ui.base.entity.Progress
import app.suhocki.mybooks.ui.catalog.entity.BannersHolder
import app.suhocki.mybooks.ui.catalog.entity.Header
import app.suhocki.mybooks.uiThread
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.jetbrains.anko.AnkoLogger
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.timer

private const val AUTO_SCROLL_TIMER_DELAY = 3000L

@InjectViewState
class CatalogPresenter @Inject constructor(
    @CategoriesObserver private val categoriesObserver: FirestoreObserver,
    @BannersObserver private val bannersObserver: FirestoreObserver,
    private val mapper: Mapper,
    private val router: FlowRouter
) : MvpPresenter<CatalogView>(), AnkoLogger {

    private val header = Header(R.string.categories)
    private var visibleBannerIndex = 0

    //region init categories paginator
    private val categoriesFactory = { page: Int ->
        categoriesObserver
            .observePage((page - 1) * LIMIT, LIMIT)
            .map { mapper.map<Category>(it) }
    }

    private val categoriesPaginatorView: FirestorePaginator.ViewController<Any> =
        object : FirestorePaginator.ViewController<Any> {
            override fun showEmptyProgress(show: Boolean) =
                viewState.showEmptyProgress(show)

            override fun showEmptyError(show: Boolean, error: Throwable?) {
                error?.let {
                    viewState.showEmptyError(show, error)
                } ?: viewState.showEmptyError(show, null)
            }

            override fun showErrorMessage(error: Throwable) =
                viewState.showErrorMessage(error)

            override fun showEmptyView(show: Boolean) =
                viewState.showEmptyView(show)

            override fun showData(show: Boolean, data: List<Any>) {
                val bannersHolder = BannersHolder(
                    mutableListOf<Any>()
                        .apply {
                            addAll(bannersPaginator.currentData)
                            if (bannersPaginator.isPageProgress) {
                                add(Progress(true))
                            }
                        }
                )
                mutableListOf<Any>()
                    .apply {
                        add(bannersHolder)
                        add(header)
                        addAll(data)
                    }.let {
                        viewState.showData(it)
                    }
            }

            override fun showRefreshProgress(show: Boolean) =
                viewState.showRefreshProgress(show)

            override fun showPageProgress(show: Boolean) {
                val bannersHolder = BannersHolder(mutableListOf<Any>()
                    .apply {
                        addAll(bannersPaginator.currentData)
                        if (bannersPaginator.isPageProgress) {
                            add(Progress(true))
                        }
                    })
                mutableListOf<Any>()
                    .apply {
                        add(bannersHolder)
                        add(header)
                        addAll(categoriesPaginator.currentData)
                        if (categoriesPaginator.isPageProgress) {
                            add(Progress())
                        }
                    }.let {
                        viewState.showData(it)
                    }
            }
        }

    private val categoriesPaginator =
        FirestorePaginator(
            categoriesObserver,
            categoriesFactory,
            categoriesPaginatorView,
            { firestoreData -> firestoreData.map { mapper.map<Category>(it) } }
        )
    //endregion

    //region init banners paginator
    private val bannersFactory = { page: Int ->
        bannersObserver
            .observePage((page - 1) * LIMIT, LIMIT)
            .map { mapper.map<Banner>(it) }
    }

    private val bannersPaginatorView: FirestorePaginator.ViewController<Any> =
        object : FirestorePaginator.ViewController<Any> {
            override fun showEmptyProgress(show: Boolean) {
//                viewState.showEmptyProgress(show)
            }

            override fun showEmptyError(show: Boolean, error: Throwable?) {
//                error?.let {
//                    viewState.showEmptyError(show, error)
//                } ?: viewState.showEmptyError(show, null)
            }

            override fun showErrorMessage(error: Throwable) {
//                viewState.showErrorMessage(error)
            }

            override fun showEmptyView(show: Boolean) {
//                viewState.showEmptyView(show)
            }

            override fun showData(show: Boolean, data: List<Any>) {
                mutableListOf<Any>()
                    .apply {
                        add(BannersHolder(data))
                        add(header)
                        addAll(categoriesPaginator.currentData)
                        if (categoriesPaginator.isPageProgress) {
                            add(Progress())
                        }
                    }.let {
                        viewState.showData(it)
                    }
            }

            override fun showRefreshProgress(show: Boolean) {
//                viewState.showRefreshProgress(show)
            }

            override fun showPageProgress(show: Boolean) {
                val bannersHolder = BannersHolder(
                    mutableListOf<Any>().apply {
                        addAll(bannersPaginator.currentData)
                        if (show) {
                            add(Progress(true))
                        }
                    }
                )
                mutableListOf<Any>()
                    .apply {
                        add(bannersHolder)
                        add(header)
                        addAll(categoriesPaginator.currentData)
                        if (categoriesPaginator.isPageProgress) {
                            add(Progress())
                        }
                    }.let {
                        viewState.showData(it)
                    }

            }
        }

    private val bannersPaginator =
        FirestorePaginator(
            bannersObserver,
            bannersFactory,
            bannersPaginatorView,
            { firestoreData -> firestoreData.map { mapper.map<Banner>(it) } }
        )
    //endregion

    private var bannersTimer: Timer? = null
    private var isNavigatingToBooks = false

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        categoriesPaginator.refresh()
        bannersPaginator.refresh()
    }

    override fun attachView(view: CatalogView?) {
        super.attachView(view)
        enableAutoScroll(true)
        visibleBannerIndex = 0
        viewState.showBannerByIndex(visibleBannerIndex)
    }

    override fun detachView(view: CatalogView?) {
        enableAutoScroll(false)
        super.detachView(view)
        if (isNavigatingToBooks) {
            viewState.animateHamburgerOnBack()
            isNavigatingToBooks = false
        }
    }

    override fun onDestroy() {
        categoriesPaginator.release()
        categoriesObserver.dispose()
        bannersPaginator.release()
        bannersObserver.dispose()
        bannersTimer?.apply { cancel(); purge() }
        super.onDestroy()
    }

    fun loadNextCategoriesPage() {
        categoriesPaginator.loadNewPage()
    }

    fun loadNextBannersPage() {
        bannersPaginator.loadNewPage()
    }

    fun onCategoryClick(category: Category) {
        isNavigatingToBooks = true
        router.navigateTo(Screens.Books(category.id))
    }

    fun setVisibleBannerIndex(index: Int) {
        visibleBannerIndex = index
    }

    fun enableAutoScroll(enable: Boolean) {
        if (bannersTimer != null && !enable) {
            bannersTimer!!.cancel()
            bannersTimer!!.purge()
            bannersTimer = null
        } else if (bannersTimer == null && enable) {
            bannersTimer = timer(
                period = AUTO_SCROLL_TIMER_DELAY,
                initialDelay = AUTO_SCROLL_TIMER_DELAY
            ) {
                flingBanners()
            }
        }
    }

    fun onBackPressed() = router.exit()

    private fun flingBanners() {
        if (bannersPaginator.currentData.isEmpty()) {
            return
        }
        val bannersCount = bannersPaginator.currentData.size
        uiThread {
            viewState.showBannerByIndex(visibleBannerIndex++ % bannersCount)
        }
    }


    companion object {
        const val LIMIT = 3
    }
}