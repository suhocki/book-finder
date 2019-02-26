package app.suhocki.mybooks.ui.drawer.navigation

import android.support.annotation.IdRes
import app.suhocki.mybooks.R
import app.suhocki.mybooks.Screens
import app.suhocki.mybooks.data.preferences.PreferencesRepository
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.domain.model.Version
import app.suhocki.mybooks.model.system.flow.FlowRouter
import app.suhocki.mybooks.presentation.global.GlobalMenuController
import app.suhocki.mybooks.ui.Ids
import app.suhocki.mybooks.ui.drawer.navigation.entity.Caption
import app.suhocki.mybooks.ui.drawer.navigation.entity.DrawerHeaderItem
import app.suhocki.mybooks.ui.drawer.navigation.entity.MenuItem
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import javax.inject.Inject

@InjectViewState
class NavigationDrawerPresenter @Inject constructor(
    private val version: Version,
    private val resourceManager: ResourceManager,
    private val preferencesRepository: PreferencesRepository,
    private val router: FlowRouter,
    private val menuController: GlobalMenuController
) : MvpPresenter<NavigationDrawerView>() {

    private val data: List<Any>
        get() = mutableListOf(
            DrawerHeaderItem(R.string.app_name),
            MenuItem(Ids.navCatalog, R.string.catalog, R.drawable.ic_catalog),
            MenuItem(Ids.navLicenses, R.string.licenses, R.drawable.ic_copyright),
            MenuItem(Ids.navChanges, R.string.changelog, R.drawable.ic_changelog),
            MenuItem(Ids.navAboutDeveloper, R.string.developer, R.drawable.ic_developer),
            Caption(resourceManager.getString(R.string.version, version.version, version.code))
        ).apply {
            if (preferencesRepository.isAdminModeEnabled) {
                add(2, MenuItem(Ids.navAdmin, R.string.admin, R.drawable.ic_admin))
            }
        }

    @IdRes
    private var selectedMenuId: Int = Ids.navCatalog

    fun selectMenuItem(@IdRes menuItemId: Int) {
        data.apply {
            filterIsInstance(MenuItem::class.java)
                .find { it.id == menuItemId }
                ?.isSelected = true
        }.let {
            viewState.showData(it)
        }
        selectedMenuId = menuItemId
    }

    fun refreshMenuItems() {
        selectMenuItem(selectedMenuId)
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

    fun onMenuItemClick(@IdRes menuItemId: Int) {
        menuController.close()
        if (selectedMenuId == menuItemId) {
            return
        }
        val screen = when (menuItemId) {
            Ids.navCatalog -> Screens.MainFlow
            Ids.navAdmin -> Screens.Admin
            Ids.navLicenses -> Screens.Licenses
            Ids.navChanges -> Screens.Changelog
            else -> Screens.Changelog
        }
        if (menuItemId == Ids.navCatalog) {
            router.backTo(Screens.CatalogFlow)
        } else {
            router.navigateTo(screen)
        }
    }
}