package app.suhocki.mybooks.ui.drawer.navigation

import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.preferences.PreferencesRepository
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.domain.model.Version
import app.suhocki.mybooks.model.system.flow.FlowRouter
import app.suhocki.mybooks.presentation.global.paginator.GlobalMenuController
import app.suhocki.mybooks.ui.drawer.navigation.entity.Caption
import app.suhocki.mybooks.ui.drawer.navigation.entity.DrawerHeaderItem
import app.suhocki.mybooks.ui.drawer.navigation.entity.MenuItem
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import javax.inject.Inject

@InjectViewState
class NavigationDrawerPresenter @Inject constructor(
    version: Version,
    resourceManager: ResourceManager,
    private val router: FlowRouter,
    private val menuController: GlobalMenuController,
    private val preferencesRepository: PreferencesRepository
) : MvpPresenter<NavigationDrawerView>() {

    private val data = mutableListOf(
        DrawerHeaderItem(R.string.app_name),
        MenuItem(R.id.nav_catalog, R.string.catalog, R.drawable.ic_catalog),
        MenuItem(R.id.nav_search, R.string.search, R.drawable.ic_search),
        MenuItem(R.id.nav_info, R.string.info, R.drawable.ic_info),
        Caption(resourceManager.getString(R.string.about)),
        MenuItem(R.id.nav_licenses, R.string.licenses, R.drawable.ic_copyright),
        MenuItem(R.id.nav_changes, R.string.changelog, R.drawable.ic_changelog),
        MenuItem(R.id.nav_about_developer, R.string.developer, R.drawable.ic_developer),
        Caption(resourceManager.getString(R.string.version, version.version, version.code), true)
    )

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.showData(data)
    }

    fun onMenuItemClick(menuItem: MenuItem) {
        menuController.close()

        data.filterIsInstance(MenuItem::class.java)
            .forEach { it.isSelected = it.id == menuItem.id }

        viewState.showData(data)
    }

    fun openAppSettings() {
        viewState.showAppSettings(
            preferencesRepository.isAdminModeEnabled,
            preferencesRepository.isDebugPanelEnabled
        )
    }

    fun applySettings(
        adminEnabled: Boolean,
        debugEnabled: Boolean
    ) {
        preferencesRepository.isAdminModeEnabled = adminEnabled
        preferencesRepository.isDebugPanelEnabled = debugEnabled
    }
}