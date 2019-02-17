package app.suhocki.mybooks.ui.main

import app.suhocki.mybooks.data.preferences.PreferencesRepository
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import javax.inject.Inject

@InjectViewState
class MainFlowPresenter @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : MvpPresenter<MainFlowView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        checkAdminMode()
    }

    fun checkAdminMode() {
        viewState.showAdminButton(preferencesRepository.isAdminModeEnabled)
    }
}