package app.suhocki.mybooks.ui.catalog

import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.error.ErrorHandler
import app.suhocki.mybooks.domain.CategoriesInteractor
import app.suhocki.mybooks.domain.model.Header
import app.suhocki.mybooks.domain.model.Search
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.concurrent.Future
import javax.inject.Inject

@InjectViewState
class CatalogPresenter @Inject constructor(
    private val interactor: CategoriesInteractor,
    private val errorHandler: ErrorHandler,
    private val search: Search,
    private val header: Header
) : MvpPresenter<CatalogView>(), AnkoLogger {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        doAsync(errorHandler.errorReceiver) {
            val catalogItems = mutableListOf<Any>().apply {
                add(interactor.getBanner())
                add(header)
                addAll(interactor.getCategories())
            }
            uiThread { viewState.showCatalogItems(catalogItems) }
        }
    }

    fun startSearchMode(): Future<Unit> {
        return doAsync(errorHandler.errorReceiver) {
            val catalogItems = mutableListOf<Any>().apply {
                add(interactor.getBanner())
                add(search)
                add(header.apply { titleRes = R.string.enter_query })
            }
            uiThread {
                viewState.showSearchMode(true)
                viewState.showCatalogItems(catalogItems, CatalogFragment.SEARCH_POSITION)
            }
        }
    }

    fun stopSearchMode() =
        doAsync(errorHandler.errorReceiver) {
            search.searchQuery = EMPTY_STRING
            val catalogItems = mutableListOf<Any>().apply {
                add(interactor.getBanner())
                add(header.apply { titleRes = R.string.catalog })
                addAll(interactor.getCategories())
            }
            uiThread {
                viewState.showSearchMode(false)
                viewState.showCatalogItems(catalogItems, CatalogFragment.BANNER_POSITION)
            }
        }

    fun search() = doAsync(errorHandler.errorReceiver) {
        val catalogItems = mutableListOf<Any>().apply {
            add(interactor.getBanner())
            add(search)
            add(header.apply { titleRes = R.string.search_results })
            addAll(interactor.search(search).apply {
                header.titleRes = if (size > 0) R.string.search_results else R.string.not_found
            })
        }
        uiThread {
            viewState.showCatalogItems(catalogItems)
        }
    }

    fun clearSearchQuery() {
        if (search.searchQuery.isBlank()) stopSearchMode()
        else {
            search.searchQuery = EMPTY_STRING
            viewState.showBlankSearch()
        }
    }

    companion object {
        const val EMPTY_STRING = ""
    }
}