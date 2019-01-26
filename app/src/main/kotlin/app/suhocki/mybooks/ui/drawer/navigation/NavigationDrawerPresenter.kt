package app.suhocki.mybooks.ui.drawer.navigation

import app.suhocki.mybooks.R
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
    private val router: FlowRouter,
    private val menuController: GlobalMenuController
) : MvpPresenter<NavigationDrawerView>() {

    private val data = mutableListOf(
        DrawerHeaderItem(),
        MenuItem(R.id.nav_catalog, R.string.catalog, R.drawable.ic_books),
        MenuItem(R.id.nav_search, R.string.search, R.drawable.ic_search),
        MenuItem(R.id.nav_info, R.string.info, R.drawable.ic_info),
        Caption(R.string.about),
        MenuItem(R.id.nav_licenses, R.string.licenses, R.drawable.ic_copyright),
        MenuItem(R.id.nav_changes, R.string.changelog, R.drawable.ic_changelog),
        MenuItem(R.id.nav_about_developer, R.string.about_developer, R.drawable.ic_developer)
    )

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.showData(data)
    }

    fun onMenuItemClick(menuItem: MenuItem) {
        menuController.close()

        val deselectedIndex = data.indexOfFirst { it is MenuItem && it.isSelected }
        if (deselectedIndex != -1) {
            data[deselectedIndex] = (data[deselectedIndex] as MenuItem).copy(isSelected = false)
        }

        val selectedIndex = data.indexOfFirst { it is MenuItem && it.id == menuItem.id }
        data[selectedIndex] = menuItem.copy(isSelected = true)

        viewState.showData(data)
    }
}