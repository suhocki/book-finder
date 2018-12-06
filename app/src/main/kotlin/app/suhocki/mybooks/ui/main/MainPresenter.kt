package app.suhocki.mybooks.ui.main

import app.suhocki.mybooks.data.preferences.PreferencesRepository
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import javax.inject.Inject

@InjectViewState
class MainPresenter @Inject constructor(
    private val settingsRepository: PreferencesRepository
) : MvpPresenter<MainView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        val mode = settingsRepository.isAdminModeEnabled
        viewState.showAdminMode(mode)
    }
}