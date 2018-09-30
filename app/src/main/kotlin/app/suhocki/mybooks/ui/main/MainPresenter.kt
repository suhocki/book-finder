package app.suhocki.mybooks.ui.main

import app.suhocki.mybooks.domain.MainInteractor
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import javax.inject.Inject

@InjectViewState
class MainPresenter @Inject constructor(
    private val mainInteractor: MainInteractor
) : MvpPresenter<MainView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        val mode = mainInteractor.isAdminModeEnabled()
        viewState.showAdminMode(mode)
    }
}