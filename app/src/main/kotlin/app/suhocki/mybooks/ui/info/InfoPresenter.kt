package app.suhocki.mybooks.ui.info

import app.suhocki.mybooks.data.remoteconfig.RemoteConfiguration
import app.suhocki.mybooks.di.ErrorReceiver
import app.suhocki.mybooks.domain.InfoInteractor
import app.suhocki.mybooks.domain.model.Version
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject

@InjectViewState
class InfoPresenter @Inject constructor(
    @ErrorReceiver private val errorReceiver: (Throwable) -> Unit,
    private val infoInteractor: InfoInteractor,
    private val remoteConfigurator: RemoteConfiguration,
    private val appVersion: Version
    ) : MvpPresenter<InfoView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadData()
    }

    fun loadData() {
        viewState.showProgress(true)
        doAsync(errorReceiver) {
            val items = mutableListOf<Any>().apply {
                addAll(infoInteractor.getShopInfoItems())

                if (remoteConfigurator.isAboutApplicationEnabled) {
                    addAll(infoInteractor.getAboutThisApplication())
                }

                add(appVersion)
            }
            uiThread {
                viewState.showProgress(false)
                viewState.showInfoItems(items)
            }
        }
    }

    fun toogleAdminMode() {
        val mode = infoInteractor.toogleAdminMode()
        viewState.showAdminMode(mode)
    }
}