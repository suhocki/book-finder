package app.suhocki.mybooks.ui.catalog

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
    private val searchEntity: Search,
    private val headerEntity: Header
) : MvpPresenter<CatalogView>(), AnkoLogger {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        doAsync(errorHandler.errorReceiver) {
            val catalogItems = getCatalogItems()
            uiThread { viewState.showCatalogItems(catalogItems) }
        }
    }

    private fun getCatalogItems(): MutableList<Any> =
        mutableListOf<Any>().apply {
            add(interactor.getBanner())
            add(headerEntity)
            addAll(interactor.getCategories())
        }

    fun addSearchEntity(list: MutableList<Any>): Future<Unit> {
        return doAsync(errorHandler.errorReceiver) {
            val newList = mutableListOf<Any>().apply {
                addAll(list)
                add(CatalogFragment.SEARCH_POSITION, searchEntity)
            }
            uiThread {
                viewState.showCatalogItems(newList, CatalogFragment.SEARCH_POSITION)
                viewState.showSearchMode(true)
            }
        }
    }

    fun removeSearchEntity(list: MutableList<Any>) =
        doAsync(errorHandler.errorReceiver) {
            searchEntity.searchQuery = EMPTY_STRING
            val newList = mutableListOf<Any>().apply {
                addAll(list)
                removeAt(CatalogFragment.SEARCH_POSITION)
            }
            uiThread {
                viewState.showCatalogItems(newList, CatalogFragment.BANNER_POSITION)
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