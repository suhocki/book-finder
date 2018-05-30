package app.suhocki.mybooks.ui.main

import android.os.Bundle
import android.view.Gravity
import app.suhocki.mybooks.R
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.ui.base.BaseFragment
import app.suhocki.mybooks.ui.base.DrawerHandler
import app.suhocki.mybooks.ui.catalog.CatalogFragment
import app.suhocki.mybooks.ui.info.InfoFragment
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.jetbrains.anko.setContentView
import toothpick.Toothpick

class MainActivity : MvpAppCompatActivity(), MainView, DrawerHandler {

    @InjectPresenter
    lateinit var presenter: MainPresenter

    private var ui = MainUI()
    private lateinit var tabs: HashMap<String, BaseFragment>
    private val tabKeys = listOf(
        tabIdToTag(R.id.nav_catalog),
        tabIdToTag(R.id.nav_search),
        tabIdToTag(R.id.nav_filter),
        tabIdToTag(R.id.nav_info)
    )
    private val fragmentTabPositions = arrayOf(
        TAB_POSITION_CATALOG,
        TAB_POSITION_INFO
    )

    @ProvidePresenter
    fun providePresenter(): MainPresenter =
        Toothpick.openScopes(DI.APP_SCOPE)
            .getInstance(MainPresenter::class.java)

    override fun onCreate(savedInstanceState: Bundle?) = with(ui) {
        super.onCreate(savedInstanceState)
        Toothpick.openScopes(DI.APP_SCOPE).apply {
            Toothpick.inject(this@MainActivity, this)
        }
        setContentView(this@MainActivity)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            drawerLayout.closeDrawers()
            true
        }
        bottomBar.setOnTabSelectedListener { position, wasSelected ->
            if (!wasSelected) {
                if (position in fragmentTabPositions) {
                    showTab(position, bottomBar.currentItem)
                    return@setOnTabSelectedListener true
                } else {
                    invokeAction(position)
                }
            }
            false
        }
        initFragments(savedInstanceState)
    }

    private fun initFragments(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            tabs = createNewFragments()
            val fragmentCatalog = tabs[tabKeys[TAB_POSITION_CATALOG]]
            val fragmentInfo = tabs[tabKeys[TAB_POSITION_INFO]]
            supportFragmentManager.beginTransaction()
                .add(R.id.id_main_container, fragmentCatalog, tabKeys[TAB_POSITION_CATALOG])
                .add(R.id.id_main_container, fragmentInfo, tabKeys[TAB_POSITION_INFO])
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
    }

    private fun showCatalogTab() {
        ui.bottomBar.currentItem = TAB_POSITION_CATALOG
        supportFragmentManager.beginTransaction()
            .show(tabs[tabKeys[TAB_POSITION_CATALOG]])
            .commit()
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

    override fun setExpanded(isExpanded: Boolean) {
        with(ui.drawerLayout) {
            if (isExpanded) openDrawer(Gravity.START)
            else closeDrawer(Gravity.START)
        }
    }

    companion object {
        private const val TAB_POSITION_CATALOG = 0
        private const val TAB_POSITION_INFO = 3
    }
}