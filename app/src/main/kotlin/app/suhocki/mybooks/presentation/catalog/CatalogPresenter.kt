package app.suhocki.mybooks.presentation.catalog

import app.suhocki.mybooks.data.error.ErrorHandler
import app.suhocki.mybooks.di.CategoriesStartFlag
import app.suhocki.mybooks.di.HeaderCatalogItem
import app.suhocki.mybooks.di.PrimitiveWrapper
import app.suhocki.mybooks.di.SearchCatalogItem
import app.suhocki.mybooks.domain.CategoriesInteractor
import app.suhocki.mybooks.domain.model.CatalogItem
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
    @CategoriesStartFlag private val startFromNotification: PrimitiveWrapper<Boolean>,
    @SearchCatalogItem private val searchCatalogItem: CatalogItem,
    @HeaderCatalogItem private val headerCatalogItem: CatalogItem
) : MvpPresenter<CatalogView>(), AnkoLogger {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        doAsync(errorHandler.errorReceiver) {
            if (startFromNotification.value) startedFromNotification()
            val catalogItems = getCatalogItems()
            uiThread { viewState.showCatalogItems(catalogItems) }
        }
    }

    private fun startedFromNotification() {
        startFromNotification.value = false
        interactor.setDatabaseLoaded()
        viewState.cancelAllNotifications()
    }

    private fun getCatalogItems(): MutableList<CatalogItem> =
        mutableListOf<CatalogItem>().apply {
            add(interactor.getBanners())
            add(searchCatalogItem)
            add(headerCatalogItem)
            addAll(interactor.getCategories())
        }
}