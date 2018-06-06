package app.suhocki.mybooks.ui.search

import app.suhocki.mybooks.data.error.ErrorHandler
import app.suhocki.mybooks.domain.SearchInteractor
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject

@InjectViewState
class SearchPresenter @Inject constructor(
    private val interactor: SearchInteractor,
    private val errorHandler: ErrorHandler
) : MvpPresenter<SearchView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        doAsync(errorHandler.errorReceiver) {
            val titleRes = interactor.getTitleRes()
            uiThread { viewState.showTitleRes(titleRes) }
            val searchItems = interactor.getSearchItems()
            uiThread {
                viewState.showSearchItems(searchItems)
                viewState.showProgressBar(false)
                if (searchItems.isEmpty()) viewState.showEmptyScreen()
            }
        }
    }
}