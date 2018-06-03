package app.suhocki.mybooks.ui.filter

import app.suhocki.mybooks.data.error.ErrorHandler
import app.suhocki.mybooks.domain.FilterInteractor
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject

@InjectViewState
class FilterPresenter @Inject constructor(
    private val filterInteractor: FilterInteractor,
    private val errorHandler: ErrorHandler
) : MvpPresenter<FilterView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        doAsync(errorHandler.errorReceiver) {
            val categories = filterInteractor.getFilterCategories()
            uiThread {
                viewState.showFilterItems(categories)
            }
        }
    }
}