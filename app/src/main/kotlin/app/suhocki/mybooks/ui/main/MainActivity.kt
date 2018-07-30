package app.suhocki.mybooks.ui.main

import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.view.Gravity
import app.suhocki.mybooks.R
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.di.module.CatalogModule
import app.suhocki.mybooks.hideKeyboard
import app.suhocki.mybooks.ui.Ids
import app.suhocki.mybooks.ui.base.BaseFragment
import app.suhocki.mybooks.ui.base.listener.OnSearchClickListener
import app.suhocki.mybooks.ui.catalog.CatalogFragment
import app.suhocki.mybooks.ui.info.InfoFragment
import app.suhocki.mybooks.ui.main.listener.NavigationHandler
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.jetbrains.anko.setContentView
import toothpick.Toothpick

class MainActivity : MvpAppCompatActivity(), MainView,
    NavigationHandler {

    @InjectPresenter
    lateinit var presenter: MainPresenter

    private val ui by lazy { MainUI() }

    private lateinit var tabs: HashMap<String, BaseFragment>
    private val tabKeys = listOf(
        tabIdToTag(Ids.navCatalog),
        tabIdToTag(Ids.navSearch),
        tabIdToTag(Ids.navInfo)
    )
    private val fragmentTabPositions = arrayOf(
        TAB_POSITION_CATALOG,
        TAB_POSITION_INFO
    )
    private val navigationPositions = mapOf(
        Ids.navSearch to TAB_POSITION_SEARCH,
        Ids.navCatalog to TAB_POSITION_CATALOG,
        Ids.navInfo to TAB_POSITION_INFO
    )

    @ProvidePresenter
    fun providePresenter(): MainPresenter =
        Toothpick.openScopes(DI.APP_SCOPE, DI.MAIN_ACTIVITY_SCOPE).apply {
            installModules(CatalogModule(this@MainActivity))
        }.getInstance(MainPresenter::class.java)


    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) Toothpick.closeScope(DI.MAIN_ACTIVITY_SCOPE)
    }

    override fun onCreate(savedInstanceState: Bundle?) = with(ui) {
        super.onCreate(savedInstanceState)
        Toothpick.openScopes(DI.APP_SCOPE).apply {
            Toothpick.inject(this@MainActivity, this)
        }
        setContentView(this@MainActivity)
        navigationView.menu.getItem(TAB_POSITION_CATALOG).isChecked = true
        navigationView.setNavigationItemSelectedListener { menuItem ->
            val newPosition = navigationPositions[menuItem.itemId]!!
            bottomBar.setCurrentItem(newPosition, true)
            drawerLayout.closeDrawers()
            false
        }
        bottomBar.setOnTabSelectedListener { position, wasSelected ->
            handleNavigationClick(position, wasSelected)
        }
        initFragments(savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
        ui.navigationView.hideKeyboard()
    }

    private fun handleNavigationClick(position: Int, wasSelected: Boolean): Boolean {
        if (!wasSelected) {
            if (position in fragmentTabPositions) {
                ui.navigationView.menu.getItem(position).isChecked = true
                showTab(position, ui.bottomBar.currentItem)
                return true
            } else {
                invokeAction(position)
            }
        }
        return false
    }

    private fun initFragments(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            tabs = createNewFragments()
            val fragmentCatalog = tabs[tabKeys[TAB_POSITION_CATALOG]]
            val fragmentInfo = tabs[tabKeys[TAB_POSITION_INFO]]
            supportFragmentManager.beginTransaction()
                .add(Ids.mainContainer, fragmentCatalog, tabKeys[TAB_POSITION_CATALOG])
                .add(Ids.mainContainer, fragmentInfo, tabKeys[TAB_POSITION_INFO])
                .hide(fragmentInfo)
                .commitNow()
            ui.bottomBar.setCurrentItem(TAB_POSITION_CATALOG, false)
        } else {
            tabs = findFragments()
        }
    }

    private fun tabIdToTag(id: Int) = "tab_$id"

    private fun showTab(newPosition: Int, oldPosition: Int) {
        supportFragmentManager.beginTransaction()
            .hide(tabs[tabKeys[oldPosition]])
            .show(tabs[tabKeys[newPosition]])
            .commit()
    }

    private fun invokeAction(position: Int) {
        showCatalogTab()
        when (position) {
            TAB_POSITION_SEARCH -> expandSearchView()
        }
    }

    private fun showCatalogTab() {
        ui.bottomBar.currentItem = TAB_POSITION_CATALOG
        supportFragmentManager.beginTransaction()
            .show(tabs[tabKeys[TAB_POSITION_CATALOG]])
            .commit()
    }

    private fun expandSearchView() {
        val onSearchClickListener = tabs[tabKeys[TAB_POSITION_CATALOG]] as OnSearchClickListener
        onSearchClickListener.onExpandSearchClick()
    }

    private fun createNewFragments(): HashMap<String, BaseFragment> = hashMapOf(
        tabKeys[TAB_POSITION_CATALOG] to CatalogFragment.newInstance(),
        tabKeys[TAB_POSITION_INFO] to InfoFragment.newInstance()
    )

    private fun findFragments(): HashMap<String, BaseFragment> = hashMapOf(
        tabKeys[TAB_POSITION_CATALOG] to supportFragmentManager
            .findFragmentByTag(tabKeys[TAB_POSITION_CATALOG]) as BaseFragment,

        tabKeys[TAB_POSITION_INFO] to supportFragmentManager
            .findFragmentByTag(tabKeys[TAB_POSITION_INFO]) as BaseFragment
    )

    override fun setDrawerExpanded(isExpanded: Boolean) {
        with(ui.drawerLayout) {
            if (isExpanded) openDrawer(Gravity.START)
            else closeDrawer(Gravity.START)
        }
    }

    override fun setDrawerEnabled(isEnabled: Boolean) {
        ui.drawerLayout.setDrawerLockMode(
            if (isEnabled) DrawerLayout.LOCK_MODE_UNLOCKED
            else DrawerLayout.LOCK_MODE_LOCKED_CLOSED
        )
    }

    override fun setBottomNavigationVisible(isVisible: Boolean) {
        with(ui.bottomBar) {
            if (isVisible) {
                isBehaviorTranslationEnabled = true
                restoreBottomNavigation()
            } else {
                isBehaviorTranslationEnabled = false
                hideBottomNavigation()
            }
        }
    }

    override fun onBackPressed() {
        val onSearchClickListener =
            tabs[tabKeys[TAB_POSITION_CATALOG]] as OnSearchClickListener

        with(ui.drawerLayout) {
            when {
                isDrawerOpen(ui.navigationView) -> closeDrawer(Gravity.START)

                ui.bottomBar.currentItem != TAB_POSITION_CATALOG -> showCatalogTab()

                !onSearchClickListener.onCollapseSearchClick() -> super.onBackPressed()
            }
        }
    }

    companion object {
        private const val TAB_POSITION_CATALOG = 0
        private const val TAB_POSITION_SEARCH = 1
        private const val TAB_POSITION_INFO = 2
    }
}