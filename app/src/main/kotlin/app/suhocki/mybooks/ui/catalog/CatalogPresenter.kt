package app.suhocki.mybooks.ui.catalog

import app.suhocki.mybooks.Screens
import app.suhocki.mybooks.data.firestore.FirestoreObserver
import app.suhocki.mybooks.data.mapper.Mapper
import app.suhocki.mybooks.di.BannersObserver
import app.suhocki.mybooks.di.CategoriesObserver
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.model.system.flow.FlowRouter
import app.suhocki.mybooks.presentation.global.paginator.FirestorePaginator
import app.suhocki.mybooks.ui.catalog.entity.UiCategory
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.google.firebase.firestore.DocumentSnapshot
import javax.inject.Inject

@InjectViewState
class CatalogPresenter @Inject constructor(
    @CategoriesObserver private val categoriesObserver: FirestoreObserver,
    @BannersObserver private val bannersObserver: FirestoreObserver,
    private val mapper: Mapper,
    private val router: FlowRouter
) : MvpPresenter<CatalogView>() {

    private val firestoreDataMapper = { firestoreData: List<DocumentSnapshot> ->
        firestoreData.map { mapper.map<UiCategory>(it) }
    }

    private val categoriesFactory = { page: Int ->
        categoriesObserver
            .observePage(page.dec() * LIMIT, LIMIT)
            .let { firestoreDataMapper.invoke(it) }
    }

    private val paginatorView = object : FirestorePaginator.ViewController<Any> {
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

        override fun showData(show: Boolean, data: List<Any>) {
            viewState.showData(data)
        }

        override fun showRefreshProgress(show: Boolean) {
            viewState.showRefreshProgress(show)
        }

        override fun showPageProgress(show: Boolean) {
            viewState.showPageProgress(show)
        }
    }

    private val paginator =
        FirestorePaginator(
            categoriesObserver,
            categoriesFactory,
            paginatorView,
            firestoreDataMapper
        )

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        paginator.refresh()
    }

    override fun onDestroy() {
        super.onDestroy()
        paginator.release()
        categoriesObserver.dispose()
    }

    fun loadNextPage() {
        paginator.loadNewPage()
    }

    fun onCategoryClick(category: Category) {
        router.navigateTo(Screens.Books(category.id))
    }

    fun onBackPressed() = router.exit()

    companion object {
        const val LIMIT = 3
    }
}