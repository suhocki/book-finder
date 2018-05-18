package app.suhocki.mybooks.presentation.categories

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import app.suhocki.mybooks.data.error.ErrorHandler
import app.suhocki.mybooks.di.CategoriesStartFlag
import app.suhocki.mybooks.di.PrimitiveWrapper
import app.suhocki.mybooks.domain.CategoriesInteractor
import javax.inject.Inject

@InjectViewState
class CategoriesPresenter @Inject constructor(
    private val interactor: CategoriesInteractor,
    private val errorHandler: ErrorHandler,
    @CategoriesStartFlag private val startFromNotification: PrimitiveWrapper<Boolean>
) : MvpPresenter<CategoriesView>(), AnkoLogger {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        doAsync(errorHandler.errorReceiver) {
            if (startFromNotification.value) {
                startFromNotification.value = false
                interactor.setDatabaseLoaded()
                viewState.cancelAllNotifications()
            }
            interactor.getCategories()
                .let { categories -> uiThread { viewState.showCategories(categories) } }
        }
    }
}