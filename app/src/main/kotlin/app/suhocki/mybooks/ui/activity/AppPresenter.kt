package app.suhocki.mybooks.ui.activity

import app.suhocki.mybooks.data.preferences.PreferencesRepository
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import javax.inject.Inject

@InjectViewState
class AppPresenter @Inject constructor(
    private val settingsRepository: PreferencesRepository
) : MvpPresenter<AppView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        val isDebugPanelEnabled = settingsRepository.isDebugPanelEnabled
        viewState.showDebugPanel(isDebugPanelEnabled)
    }
}