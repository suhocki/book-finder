package app.suhocki.mybooks.ui.main

import android.os.Bundle
import app.suhocki.mybooks.R
import app.suhocki.mybooks.Screens
import app.suhocki.mybooks.ui.Ids
import app.suhocki.mybooks.ui.base.BaseFragment
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import ru.terrakok.cicerone.android.support.SupportAppScreen
import toothpick.Toothpick

class MainFlowFragment : BaseFragment<MainFlowUI>() {

    private val currentTabFragment: BaseFragment<*>?
        get() = childFragmentManager.fragments.firstOrNull { !it.isHidden } as? BaseFragment<*>

    override val ui by lazy { MainFlowUI() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toothpick.inject(this, scope)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val navigationAdapter = AHBottomNavigationAdapter(activity, R.menu.bottom_menu)
        navigationAdapter.setupWithBottomNavigation(ui.bottomBar)

        ui.bottomBar.setOnTabSelectedListener { position, wasSelected ->
            if (currentTabFragment == null || !wasSelected) {
                showScreen(
                    when (position) {
                        0 -> catalogTab
                        1 -> searchTab
                        2 -> infoTab
                        else -> catalogTab
                    }
                )
            }
            true
        }

        ui.bottomBar.setCurrentItem(
            when (currentTabFragment?.tag) {
                catalogTab.screenKey -> 0
                searchTab.screenKey -> 1
                infoTab.screenKey -> 2
                else -> 0
            },
            true
        )
    }

    private fun showScreen(screen: SupportAppScreen) {
        val currentFragment = currentTabFragment
        val newFragment = childFragmentManager.findFragmentByTag(screen.screenKey)

        if (currentFragment != null && newFragment != null && currentFragment == newFragment) return

        childFragmentManager.beginTransaction().apply {
            if (newFragment == null) add(
                Ids.mainFlowContainer,
                createTabFragment(screen),
                screen.screenKey
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

    companion object {
        private val catalogTab = Screens.CatalogFlow
        private val searchTab = Screens.SearchFlow
        private val infoTab = Screens.InfoFlow
    }
}