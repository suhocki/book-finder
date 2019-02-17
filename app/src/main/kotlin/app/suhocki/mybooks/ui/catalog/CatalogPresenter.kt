package app.suhocki.mybooks.ui.catalog

import app.suhocki.mybooks.Screens
import app.suhocki.mybooks.data.firestore.FirestoreObserver
import app.suhocki.mybooks.data.mapper.Mapper
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.model.system.flow.FlowRouter
import app.suhocki.mybooks.presentation.global.paginator.FirestorePaginator
import app.suhocki.mybooks.ui.catalog.entity.UiCategory
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import javax.inject.Inject

@InjectViewState
class CatalogPresenter @Inject constructor(
    private val firestoreObserver: FirestoreObserver,
    private val mapper: Mapper,
    private val router: FlowRouter
) : MvpPresenter<CatalogView>() {

    private val categoriesFactory: (Int) -> List<UiCategory> = { page: Int ->
        val snapshot = firestoreObserver.observePage(
            page.dec() * LIMIT,
            LIMIT
        )
        val pageData = snapshot
            .mapTo(mutableListOf()) { mapper.map<UiCategory>(it) }
        pageData
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

    private val paginator = FirestorePaginator(
        firestoreObserver,
        categoriesFactory,
        paginatorView,
        { firestoreData -> firestoreData.map { mapper.map<UiCategory>(it) } }
    )

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        paginator.refresh()
    }

    override fun onDestroy() {
        super.onDestroy()
        paginator.release()
        firestoreObserver.dispose()
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