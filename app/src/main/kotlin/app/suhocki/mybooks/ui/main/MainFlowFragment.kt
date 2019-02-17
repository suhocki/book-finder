package app.suhocki.mybooks.ui.main

import android.os.Bundle
import android.support.annotation.IdRes
import app.suhocki.mybooks.R
import app.suhocki.mybooks.Screens
import app.suhocki.mybooks.presentation.global.GlobalMenuController
import app.suhocki.mybooks.ui.Ids
import app.suhocki.mybooks.ui.base.BaseFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import ru.terrakok.cicerone.android.support.SupportAppScreen
import toothpick.Toothpick
import javax.inject.Inject

class MainFlowFragment : BaseFragment<MainFlowUI>(), MainFlowView {

    @InjectPresenter
    lateinit var presenter: MainFlowPresenter

    @ProvidePresenter
    fun providePresenter(): MainFlowPresenter =
        scope.getInstance(MainFlowPresenter::class.java)

    private val currentTabFragment: BaseFragment<*>?
        get() = childFragmentManager.fragments.firstOrNull { !it.isHidden } as? BaseFragment<*>

    override val ui by lazy { MainFlowUI() }

    @Inject
    lateinit var menuController: GlobalMenuController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toothpick.inject(this, scope)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val navigationAdapter = AHBottomNavigationAdapter(activity, R.menu.bottom_menu)
        navigationAdapter.setupWithBottomNavigation(ui.bottomBar)

        ui.bottomBar.setOnTabSelectedListener { position, wasSelected ->
            if (!wasSelected) {
                selectTab(
                    when (position) {
                        0 -> catalogTab
                        1 -> searchTab
                        2 -> infoTab
                        3 -> adminTab
                        else -> catalogTab
                    }
                )
                menuController.selectMenuItem(
                    when (position) {
                        0 -> R.id.nav_catalog
                        1 -> R.id.nav_search
                        2 -> R.id.nav_info
                        3 -> R.id.nav_admin
                        else -> R.id.nav_catalog
                    }
                )
            }
            true
        }

        selectTab(
            when (currentTabFragment?.tag) {
                catalogTab.screenKey -> catalogTab
                infoTab.screenKey -> infoTab
                searchTab.screenKey -> searchTab
                adminTab.screenKey -> adminTab
                else -> catalogTab
            }
        )
    }

    override fun onResume() {
        super.onResume()
        menuController.onMenuItemSelectedListeners.add(::selectMenuItem)
        menuController.onAdminModeChangedReceivers.add(presenter::checkAdminMode)
    }

    override fun onPause() {
        super.onPause()
        menuController.onMenuItemSelectedListeners.remove(::selectMenuItem)
        menuController.onAdminModeChangedReceivers.remove(presenter::checkAdminMode)
    }

    private fun selectMenuItem(@IdRes menuItemId: Int) {
        ui.bottomBar.setCurrentItem(
            when (menuItemId) {
                R.id.nav_catalog -> 0
                R.id.nav_search -> 1
                R.id.nav_info -> 2
                R.id.nav_admin -> 3
                else -> 0
            }, false
        )

        selectTab(
            when (menuItemId) {
                R.id.nav_catalog -> catalogTab
                R.id.nav_search -> searchTab
                R.id.nav_info -> infoTab
                R.id.nav_admin -> adminTab
                else -> catalogTab
            }
        )
    }

    private fun selectTab(tab: SupportAppScreen) {
        val currentFragment = currentTabFragment
        val newFragment = childFragmentManager.findFragmentByTag(tab.screenKey)

        if (currentFragment != null && newFragment != null && currentFragment == newFragment) return

        childFragmentManager.beginTransaction().apply {
            if (newFragment == null) add(
                Ids.mainFlowContainer,
                createTabFragment(tab),
                tab.screenKey
            )

            currentFragment?.let {
                hide(it)
                it.userVisibleHint = false
            }
            newFragment?.let {
                show(it)
                it.userVisibleHint = true
            }
        }.commitNow()
    }


    private fun createTabFragment(tab: SupportAppScreen) = tab.fragment

    override fun onBackPressed() {
        currentTabFragment?.onBackPressed()
    }

    override fun showAdminButton(show: Boolean) {
        val containsAdminTab = ui.bottomBar.getItem(3) != null
        if (show && !containsAdminTab) {
            ui.bottomBar.addItem(AHBottomNavigationItem(R.string.admin, R.drawable.ic_admin, 0))
            ui.bottomBar.restoreBottomNavigation()
        } else if (!show && containsAdminTab) {
            ui.bottomBar.removeItemAtIndex(3)
        }
    }

    companion object {
        private val catalogTab = Screens.CatalogFlow
        private val searchTab = Screens.SearchFlow
        private val infoTab = Screens.InfoFlow
        private val adminTab = Screens.AdminFlow
    }
}