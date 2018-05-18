package suhockii.dev.bookfinder.presentation.categories

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import suhockii.dev.bookfinder.data.error.ErrorHandler
import suhockii.dev.bookfinder.di.CategoriesStartFlag
import suhockii.dev.bookfinder.di.PrimitiveWrapper
import suhockii.dev.bookfinder.domain.CategoriesInteractor
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