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
            add(searchEntity)
            add(headerEntity)
            addAll(interactor.getCategories())
        }
}