package app.suhocki.mybooks.ui.catalog

import app.suhocki.mybooks.Screens
import app.suhocki.mybooks.data.firestore.FirestoreObserver
import app.suhocki.mybooks.data.mapper.Mapper
import app.suhocki.mybooks.di.BannersObserver
import app.suhocki.mybooks.di.CategoriesObserver
import app.suhocki.mybooks.domain.model.Banner
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.model.system.flow.FlowRouter
import app.suhocki.mybooks.presentation.global.paginator.FirestorePaginator
import app.suhocki.mybooks.ui.catalog.entity.BannersHolder
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import javax.inject.Inject

@InjectViewState
class CatalogPresenter @Inject constructor(
    @CategoriesObserver private val categoriesObserver: FirestoreObserver,
    @BannersObserver private val bannersObserver: FirestoreObserver,
    private val mapper: Mapper,
    private val router: FlowRouter
) : MvpPresenter<CatalogView>() {

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
                val resultData =
                    mutableListOf<Any>(BannersHolder(bannersPaginator.currentData))
                        .apply { addAll(data) }
                viewState.showData(resultData)
            }

            override fun showRefreshProgress(show: Boolean) =
                viewState.showRefreshProgress(show)

            override fun showPageProgress(show: Boolean) =
                viewState.showPageProgress(show)
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
                val resultData = mutableListOf<Any>(BannersHolder(data))
                    .apply { addAll(categoriesPaginator.currentData) }
                viewState.showData(resultData)
            }

            override fun showRefreshProgress(show: Boolean) {
//                viewState.showRefreshProgress(show)
            }

            override fun showPageProgress(show: Boolean) {
//                viewState.showPageProgress(show)
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

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        categoriesPaginator.refresh()
        bannersPaginator.refresh()
    }

    override fun onDestroy() {
        super.onDestroy()
        categoriesPaginator.release()
        categoriesObserver.dispose()
        bannersPaginator.release()
        bannersObserver.dispose()
    }

    fun loadNextCategoriesPage() {
        categoriesPaginator.loadNewPage()
    }

    fun loadNextBannersPage() {
        bannersPaginator.loadNewPage()
    }

    fun onCategoryClick(category: Category) {
        router.navigateTo(Screens.Books(category.id))
    }

    fun onBackPressed() = router.exit()

    companion object {
        const val LIMIT = 3
    }
}