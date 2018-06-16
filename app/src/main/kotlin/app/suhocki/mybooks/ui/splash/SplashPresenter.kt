package app.suhocki.mybooks.ui.splash

import app.suhocki.mybooks.data.database.BooksDatabase
import app.suhocki.mybooks.data.error.ErrorHandler
import app.suhocki.mybooks.domain.SplashInteractor
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.jetbrains.anko.doAsync
import javax.inject.Inject

@InjectViewState
class SplashPresenter @Inject constructor(
    private val interactor: SplashInteractor,
    private val errorHandler: ErrorHandler
) : MvpPresenter<SplashView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        doAsync(errorHandler.errorReceiver) {
            if (interactor.isSuitableDatabaseVersion(BooksDatabase.DATABASE_VERSION)) {
                viewState.showMainScreen()
            } else {
                interactor.resetDownloadStatistics()
                viewState.showInitializationScreen()
            }
        }
    }
}