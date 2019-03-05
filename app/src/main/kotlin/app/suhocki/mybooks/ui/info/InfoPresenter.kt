package app.suhocki.mybooks.ui.info

import app.suhocki.mybooks.Screens
import app.suhocki.mybooks.data.mapper.Mapper
import app.suhocki.mybooks.data.preferences.PreferencesRepository
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.di.ErrorReceiver
import app.suhocki.mybooks.di.Room
import app.suhocki.mybooks.domain.model.Version
import app.suhocki.mybooks.domain.repository.InfoRepository
import app.suhocki.mybooks.model.system.flow.FlowRouter
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject

@InjectViewState
class InfoPresenter @Inject constructor(
    @ErrorReceiver private val errorReceiver: (Throwable) -> Unit,
    private val appVersion: Version,
    @Room private val infoRepository: InfoRepository,
    private val preferences: PreferencesRepository,
    private val resourceManager: ResourceManager,
    private val mapper: Mapper,
    private val router: FlowRouter
) : MvpPresenter<InfoView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadData()
    }

    fun loadData() {
        viewState.showProgress(true)
        doAsync(errorReceiver) {
            val items = infoRepository.getShopInfo()?.let { mapper.map<List<Any>>(it) }
            uiThread {
                viewState.showProgress(false)
                if (items != null) {
                    viewState.showInfoItems(items)
                }
            }
        }
    }

    fun onVersionLongClick() {
        viewState.showAppSettingsDialog(
            preferences.isAdminModeEnabled,
            preferences.isDebugPanelEnabled
        )
    }

    fun onLicensesClick() {
        router.navigateTo(Screens.Licenses)
    }

    fun onChangelogClick() {
        router.navigateTo(Screens.Changelog)
    }

    fun updateAppSettings(adminEnabled: Boolean, debugEnabled: Boolean) {
        preferences.isDebugPanelEnabled = debugEnabled
        preferences.isAdminModeEnabled = adminEnabled

        viewState.showAdminMode(adminEnabled)
        viewState.showDebugPanel(debugEnabled)
    }

    fun onBackPressed() = router.exit()
}