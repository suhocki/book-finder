package app.suhocki.mybooks.presentation.splash

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.jetbrains.anko.doAsync
import app.suhocki.mybooks.data.error.ErrorHandler
import app.suhocki.mybooks.domain.SplashInteractor
import javax.inject.Inject

@InjectViewState
class SplashPresenter @Inject constructor(
    private val interactor: SplashInteractor,
    private val errorHandler: ErrorHandler
) : MvpPresenter<SplashView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        doAsync(errorHandler.errorReceiver) {
            if (interactor.isDataLoaded()) viewState.showMainScreen()
            else viewState.showInitializationScreen()
        }
    }
}