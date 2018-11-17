package app.suhocki.mybooks.ui.info

import app.suhocki.mybooks.data.remoteconfig.RemoteConfiguration
import app.suhocki.mybooks.data.service.ServiceHandler
import app.suhocki.mybooks.di.ErrorReceiver
import app.suhocki.mybooks.domain.InfoInteractor
import app.suhocki.mybooks.domain.model.Version
import app.suhocki.mybooks.ui.firestore.FirestoreService
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
    private val serviceHandler: ServiceHandler,
    private val appVersion: Version
) : MvpPresenter<InfoView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        serviceHandler.startUpdateService(FirestoreService.Command.PULL_SHOP_INFO)

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