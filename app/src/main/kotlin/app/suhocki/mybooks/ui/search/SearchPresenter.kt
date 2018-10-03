package app.suhocki.mybooks.ui.search

import android.os.Parcelable
import app.suhocki.mybooks.data.error.ErrorHandler
import app.suhocki.mybooks.di.ErrorReceiver
import app.suhocki.mybooks.di.SearchKey
import app.suhocki.mybooks.domain.SearchInteractor
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject

@InjectViewState
class SearchPresenter @Inject constructor(
    @ErrorReceiver private val errorReceiver: (Throwable) -> Unit,
    private val interactor: SearchInteractor,
    @SearchKey private val searchKey: String
    ) : MvpPresenter<SearchView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        doAsync(errorReceiver) {
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

    fun handleClickedItem(parcelable: Parcelable) {
        viewState.finishWithResult(searchKey, parcelable)
    }
}