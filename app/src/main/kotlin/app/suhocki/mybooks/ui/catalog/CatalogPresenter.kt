package app.suhocki.mybooks.ui.catalog

import app.suhocki.mybooks.data.error.ErrorHandler
import app.suhocki.mybooks.domain.CategoriesInteractor
import app.suhocki.mybooks.domain.model.Header
import app.suhocki.mybooks.domain.model.Hint
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
    private val header: Header,
    private val hint: Hint
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
                add(hint)
            }
            uiThread {
                viewState.showCatalogItems(catalogItems, CatalogFragment.SEARCH_POSITION)
                viewState.showSearchMode(true)
            }
        }
    }

    fun stopSearchMode() =
        doAsync(errorHandler.errorReceiver) {
            search.searchQuery = EMPTY_STRING
            val catalogItems = mutableListOf<Any>().apply {
                add(interactor.getBanner())
                add(header)
                addAll(interactor.getCategories())
            }
            uiThread {
                viewState.showCatalogItems(catalogItems, CatalogFragment.BANNER_POSITION)
                viewState.showSearchMode(false)
            }
        }

    fun clearScrollToPositionCommand(catalogItems: List<Any>) =
        doAsync {
            uiThread { viewState.showCatalogItems(catalogItems) }
        }

    companion object {
        const val EMPTY_STRING = ""
    }
}