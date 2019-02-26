package app.suhocki.mybooks.ui.info

import app.suhocki.mybooks.R
import app.suhocki.mybooks.Screens
import app.suhocki.mybooks.data.mapper.Mapper
import app.suhocki.mybooks.data.preferences.PreferencesRepository
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.di.ErrorReceiver
import app.suhocki.mybooks.di.Room
import app.suhocki.mybooks.domain.model.Info
import app.suhocki.mybooks.domain.model.Version
import app.suhocki.mybooks.domain.repository.InfoRepository
import app.suhocki.mybooks.model.system.flow.FlowRouter
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
            val items = mutableListOf<Any>().apply {
                addAll(getShopInfoItems())
                addAll(getAboutThisApplication())
                add(appVersion)
            }
            uiThread {
                viewState.showProgress(false)
                viewState.showInfoItems(items)
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
            resourceManager.getString(R.string.licenses),
            iconRes = R.drawable.ic_copyright
        ),
        InfoEntity(
            Info.InfoType.CHANGELOG,
            resourceManager.getString(R.string.changelog),
            iconRes = R.drawable.ic_changelog
        ),
        InfoEntity(
            Info.InfoType.ABOUT_DEVELOPER,
            resourceManager.getString(R.string.developer),
            iconRes = R.drawable.ic_developer
        )
    )

    fun updateAppSettings(adminEnabled: Boolean, debugEnabled: Boolean) {
        preferences.isDebugPanelEnabled = debugEnabled
        preferences.isAdminModeEnabled = adminEnabled

        viewState.showAdminMode(adminEnabled)
        viewState.showDebugPanel(debugEnabled)
    }

    fun onBackPressed() = router.exit()
}