package app.suhocki.mybooks.ui.main

import android.os.Bundle
import android.support.annotation.IntDef
import android.support.design.internal.NavigationMenu
import android.support.transition.TransitionManager
import android.support.v4.widget.DrawerLayout
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import app.suhocki.mybooks.BuildConfig
import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.remoteconfig.RemoteConfiguration
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.domain.model.Version
import app.suhocki.mybooks.hideKeyboard
import app.suhocki.mybooks.openLink
import app.suhocki.mybooks.ui.Ids
import app.suhocki.mybooks.ui.admin.AdminFragment
import app.suhocki.mybooks.ui.base.BaseFragment
import app.suhocki.mybooks.ui.base.eventbus.ActiveConnectionsCountEvent
import app.suhocki.mybooks.ui.base.listener.OnSearchClickListener
import app.suhocki.mybooks.ui.catalog.CatalogFragment
import app.suhocki.mybooks.ui.changelog.ChangelogActivity
import app.suhocki.mybooks.ui.info.InfoFragment
import app.suhocki.mybooks.ui.licenses.LicensesActivity
import app.suhocki.mybooks.ui.main.MainActivity.TabPositions.TAB_POSITION_ADMIN
import app.suhocki.mybooks.ui.main.MainActivity.TabPositions.TAB_POSITION_CATALOG
import app.suhocki.mybooks.ui.main.MainActivity.TabPositions.TAB_POSITION_INFO
import app.suhocki.mybooks.ui.main.MainActivity.TabPositions.TAB_POSITION_SEARCH
import app.suhocki.mybooks.ui.main.listener.NavigationHandler
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.itemsSequence
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.startActivity
import toothpick.Toothpick
import javax.inject.Inject


class MainActivity : MvpAppCompatActivity(), MainView,
    NavigationHandler {

    @InjectPresenter
    lateinit var presenter: MainPresenter

    @Inject
    lateinit var appVersion: Version

    @Inject
    lateinit var remoteConfiguration: RemoteConfiguration

    private val ui by lazy { MainUI() }

    private lateinit var tabs: HashMap<String, BaseFragment>

    private val tabKeys = listOf(
        tabIdToTag(Ids.navCatalog),
        tabIdToTag(Ids.navSearch),
        tabIdToTag(Ids.navInfo),
        tabIdToTag(Ids.navAdmin)
    )

    private val fragmentTabPositions = arrayOf(
        TAB_POSITION_CATALOG,
        TAB_POSITION_INFO,
        TAB_POSITION_ADMIN
    )

    private val navigationPositions = mapOf(
        R.id.nav_search to TAB_POSITION_SEARCH,
        R.id.nav_catalog to TAB_POSITION_CATALOG,
        R.id.nav_info to TAB_POSITION_INFO,
        R.id.nav_admin to TAB_POSITION_ADMIN
    )

    private val scope by lazy { Toothpick.openScopes(DI.APP_SCOPE) }

    @ProvidePresenter
    fun providePresenter(): MainPresenter =
        scope.getInstance(MainPresenter::class.java)

    override fun onCreate(savedInstanceState: Bundle?) = with(ui) {
        super.onCreate(savedInstanceState)
        Toothpick.inject(this@MainActivity, scope)

        setContentView(this@MainActivity)
        navigationView.inflateMenu(
            if (remoteConfiguration.isAboutApplicationEnabled) R.menu.drawer_menu_with_about
            else R.menu.drawer_menu
        )
        navigationView.menu.getItem(TAB_POSITION_CATALOG).isChecked = true
        (navigationView.menu as NavigationMenu).itemsSequence().forEach {
            if (it.itemId == R.id.nav_version_number) {
                it.title = getString(R.string.version, appVersion.version, appVersion.code)
            }
        }
        navigationView.setNavigationItemSelectedListener { onDrawerItemClick(it) }
        bottomBar.setOnTabSelectedListener { position, wasSelected ->
            handleNavigationClick(position, wasSelected)
        }
        initFragments(savedInstanceState, intent.getIntExtra(SCREEN_TAG, TAB_POSITION_CATALOG))
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onActiveConnectionsCountChanged(event: ActiveConnectionsCountEvent) {
        presenter.updateActiveConnectionsCount(event.count)
    }

    private fun MainUI.onDrawerItemClick(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.nav_about_developer -> openLink(BuildConfig.ABOUT_DEVELOPER_URL)

            R.id.nav_licenses -> startActivity<LicensesActivity>()

            R.id.nav_changes -> startActivity<ChangelogActivity>()

            else -> {
                val newPosition = navigationPositions[menuItem.itemId]!!
                bottomBar.setCurrentItem(newPosition, true)
                drawerLayout.closeDrawers()
            }
        }
        return false
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

    private fun initFragments(
        savedInstanceState: Bundle?,
        @TabPosition tabPosition: Int
    ) {
        if (savedInstanceState == null) {
            tabs = createNewFragments()
            val startFragment = tabs[tabKeys[tabPosition]]!!
            supportFragmentManager.beginTransaction()
                .add(Ids.mainContainer, startFragment, tabKeys[tabPosition])
                .commitNow()
            with(ui.bottomBar) {
                post { setCurrentItem(tabPosition, false) }
            }
        } else {
            tabs = findFragments()
        }
    }

    private fun tabIdToTag(id: Int) = "tab_$id"

    private fun showTab(newPosition: Int, oldPosition: Int) {
        val newFragment = supportFragmentManager.findFragmentByTag(tabKeys[newPosition])

        supportFragmentManager.beginTransaction().apply {
            if (newFragment == null)
                add(Ids.mainContainer, tabs[tabKeys[newPosition]]!!, tabKeys[newPosition])

            hide(tabs[tabKeys[oldPosition]]!!)
            show(tabs[tabKeys[newPosition]]!!)
        }.commitNow()
    }

    private fun invokeAction(position: Int) {
        if (position == TAB_POSITION_SEARCH) {
            val catalogFragment = supportFragmentManager
                .findFragmentByTag(tabKeys[TAB_POSITION_CATALOG])

            if (catalogFragment == null) {
                tabs[tabKeys[TAB_POSITION_CATALOG]] = CatalogFragment.newInstance(true)
            }
            showCatalogTab()
            expandSearchView()
        }
    }

    private fun showCatalogTab() {
        ui.bottomBar.currentItem = TAB_POSITION_CATALOG
        supportFragmentManager.beginTransaction()
            .show(tabs[tabKeys[TAB_POSITION_CATALOG]]!!)
            .commit()
    }

    private fun expandSearchView() {
        with(tabs[tabKeys[TAB_POSITION_CATALOG]] as OnSearchClickListener) {
            onExpandSearchClick()
        }
    }

    private fun createNewFragments(): HashMap<String, BaseFragment> = hashMapOf(
        tabKeys[TAB_POSITION_CATALOG] to CatalogFragment.newInstance(),

        tabKeys[TAB_POSITION_INFO] to InfoFragment.newInstance(),

        tabKeys[TAB_POSITION_ADMIN] to AdminFragment.newInstance()
    )

    private fun findFragments(): HashMap<String, BaseFragment> = hashMapOf(
        tabKeys[TAB_POSITION_CATALOG] to (supportFragmentManager
            .findFragmentByTag(tabKeys[TAB_POSITION_CATALOG])?.let { it as BaseFragment }
            ?: CatalogFragment.newInstance()),

        tabKeys[TAB_POSITION_INFO] to (supportFragmentManager
            .findFragmentByTag(tabKeys[TAB_POSITION_INFO])?.let { it as BaseFragment }
            ?: InfoFragment.newInstance()),

        tabKeys[TAB_POSITION_ADMIN] to (supportFragmentManager
            .findFragmentByTag(tabKeys[TAB_POSITION_ADMIN])?.let { it as BaseFragment }
            ?: AdminFragment.newInstance())
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

    override fun showAdminMode(enabled: Boolean) = with(ui) {
        val containsAdminTab = bottomBar.getItem(TAB_POSITION_ADMIN) != null
        if (enabled && !containsAdminTab) {
            bottomBar.addItem(AHBottomNavigationItem(R.string.admin, R.drawable.ic_admin, 0))
            bottomBar.restoreBottomNavigation()
            navigationView.menu.getItem(TAB_POSITION_ADMIN).isVisible = true
        } else if (!enabled && containsAdminTab) {
            bottomBar.removeItemAtIndex(TAB_POSITION_ADMIN)
            navigationView.menu.getItem(TAB_POSITION_ADMIN).isVisible = false
        }
    }

    override fun showDebugPanel(show: Boolean) {
        TransitionManager.beginDelayedTransition(ui.parent)

        ui.activeConnections.visibility =
                if (show) View.VISIBLE
                else View.GONE
    }

    override fun showActiveConnectionsCount(count: Int) {
        val text = resources.getString(R.string.active_connections, count)
        ui.activeConnections.text = text
    }


    companion object {
        const val SCREEN_TAG = "Screen Tag"
    }

    object TabPositions {
        const val TAB_POSITION_CATALOG = 0
        const val TAB_POSITION_SEARCH = 1
        const val TAB_POSITION_INFO = 2
        const val TAB_POSITION_ADMIN = 3
    }

    @Retention
    @IntDef(
        MainActivity.TabPositions.TAB_POSITION_CATALOG,
        MainActivity.TabPositions.TAB_POSITION_SEARCH,
        MainActivity.TabPositions.TAB_POSITION_INFO,
        MainActivity.TabPositions.TAB_POSITION_ADMIN
    )
    annotation class TabPosition
}