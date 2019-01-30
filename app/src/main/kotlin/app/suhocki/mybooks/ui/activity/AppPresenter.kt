package app.suhocki.mybooks.ui.activity

import app.suhocki.mybooks.data.preferences.PreferencesRepository
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import javax.inject.Inject

@InjectViewState
class AppPresenter @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : MvpPresenter<AppView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        val isDebugPanelEnabled = preferencesRepository.isDebugPanelEnabled
        viewState.showDebugPanel(isDebugPanelEnabled)
    }
}