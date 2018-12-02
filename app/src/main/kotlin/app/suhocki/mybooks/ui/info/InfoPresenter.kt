package app.suhocki.mybooks.ui.info

import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.mapper.Mapper
import app.suhocki.mybooks.data.remoteconfig.RemoteConfiguration
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.data.service.ServiceHandler
import app.suhocki.mybooks.di.ErrorReceiver
import app.suhocki.mybooks.di.Room
import app.suhocki.mybooks.domain.model.Info
import app.suhocki.mybooks.domain.model.Version
import app.suhocki.mybooks.domain.repository.InfoRepository
import app.suhocki.mybooks.domain.repository.SettingsRepository
import app.suhocki.mybooks.ui.firestore.FirestoreService
import app.suhocki.mybooks.ui.info.entity.HeaderEntity
import app.suhocki.mybooks.ui.info.entity.InfoEntity
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject

@InjectViewState
class InfoPresenter @Inject constructor(
    @ErrorReceiver private val errorReceiver: (Throwable) -> Unit,
    private val remoteConfigurator: RemoteConfiguration,
    private val serviceHandler: ServiceHandler,
    private val appVersion: Version,
    @Room private val infoRepository: InfoRepository,
    private val settingsRepository: SettingsRepository,
    private val resourceManager: ResourceManager,
    private val mapper: Mapper
) : MvpPresenter<InfoView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        serviceHandler.startFirestoreService(FirestoreService.Command.FETCH_SHOP_INFO)

        loadData()
    }

    fun loadData() {
        viewState.showProgress(true)
        doAsync(errorReceiver) {
            val items = mutableListOf<Any>().apply {
                addAll(getShopInfoItems())

                if (remoteConfigurator.isAboutApplicationEnabled) {
                    addAll(getAboutThisApplication())
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
        val mode = getToggledAdminMode()
        viewState.showAdminMode(mode)
    }

    private fun getToggledAdminMode(): Boolean {
        settingsRepository.isAdminModeEnabled = !settingsRepository.isAdminModeEnabled
        return settingsRepository.isAdminModeEnabled
    }

    private fun getShopInfoItems() =
        mutableListOf<Any>().apply {
            infoRepository.getShopInfo()?.let {
                addAll(mapper.map<List<Any>>(it))
            }
        }

    private fun getAboutThisApplication() = listOf(
        HeaderEntity(
            resourceManager.getString(R.string.about_this_application),
            true
        ),
        InfoEntity(
            Info.InfoType.LICENSES,
            resourceManager.getString(R.string.licences),
            iconRes = R.drawable.ic_copyright
        ),
        InfoEntity(
            Info.InfoType.CHANGELOG,
            resourceManager.getString(R.string.changelog),
            iconRes = R.drawable.ic_changelog
        ),
        InfoEntity(
            Info.InfoType.ABOUT_DEVELOPER,
            resourceManager.getString(R.string.about_developer),
            iconRes = R.drawable.ic_developer
        )
    )
}