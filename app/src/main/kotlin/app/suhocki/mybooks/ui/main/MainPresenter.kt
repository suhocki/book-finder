package app.suhocki.mybooks.ui.main

import app.suhocki.mybooks.data.firestore.FirestoreObserver
import app.suhocki.mybooks.data.preferences.PreferencesRepository
import app.suhocki.mybooks.uiThread
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import javax.inject.Inject

@InjectViewState
class MainPresenter @Inject constructor(
    private val settingsRepository: PreferencesRepository,
    private val firestoreObserver: FirestoreObserver
) : MvpPresenter<MainView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        val mode = settingsRepository.isAdminModeEnabled
        viewState.showAdminMode(mode)

        firestoreObserver.observersCountListener = {
            uiThread {
                viewState.showSimultaneousConnectionsCount(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        firestoreObserver.observersCountListener = null
    }
}