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
        tabIdToFragmentTag(R.id.nav_catalog),
        tabIdToFragmentTag(R.id.nav_search),
        tabIdToFragmentTag(R.id.nav_filter),
        tabIdToFragmentTag(R.id.nav_info)
    )

    @ProvidePresenter
    fun providePresenter(): MainPresenter =
        Toothpick.openScopes(DI.APP_SCOPE)
            .getInstance(MainPresenter::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toothpick.openScopes(DI.APP_SCOPE).apply {
            Toothpick.inject(this@MainActivity, this)
        }
        initUi()
        initFragments(savedInstanceState)
    }

    private fun initUi() {
        with(ui) {
            setContentView(this@MainActivity)
            navigationView.setNavigationItemSelectedListener { menuItem ->
                menuItem.isChecked = true
                drawerLayout.closeDrawers()
                true
            }
            bottomBar.setOnTabSelectedListener { position, wasSelected ->
                if (!wasSelected) showTab(position, bottomBar.currentItem)
                true
            }
        }
    }

    private fun initFragments(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            tabs = createNewFragments()
            supportFragmentManager.beginTransaction()
                .add(R.id.id_main_container, tabs[tabKeys[0]], tabKeys[0])
                .add(R.id.id_main_container, tabs[tabKeys[1]], tabKeys[1])
                .add(R.id.id_main_container, tabs[tabKeys[2]], tabKeys[2])
                .add(R.id.id_main_container, tabs[tabKeys[3]], tabKeys[3])
                .hide(tabs[tabKeys[1]])
                .hide(tabs[tabKeys[2]])
                .hide(tabs[tabKeys[3]])
                .commitNow()
            ui.bottomBar.setCurrentItem(0, false)
        } else {
            tabs = findFragments()
        }
    }

    private fun tabIdToFragmentTag(id: Int) = "tab_$id"

    private fun showTab(newItem: Int, oldItem: Int) {
        supportFragmentManager.beginTransaction()
            .hide(tabs[tabKeys[oldItem]])
            .show(tabs[tabKeys[newItem]])
            .commit()
    }

    private fun createNewFragments(): HashMap<String, BaseFragment> = hashMapOf(
        tabKeys[0] to CatalogFragment.newInstance(),
        tabKeys[1] to CatalogFragment.newInstance(),
        tabKeys[2] to CatalogFragment.newInstance(),
        tabKeys[3] to InfoFragment.newInstance()
    )

    private fun findFragments(): HashMap<String, BaseFragment> = hashMapOf(
        tabKeys[0] to supportFragmentManager.findFragmentByTag(tabKeys[0]) as BaseFragment,
        tabKeys[1] to supportFragmentManager.findFragmentByTag(tabKeys[1]) as BaseFragment,
        tabKeys[2] to supportFragmentManager.findFragmentByTag(tabKeys[2]) as BaseFragment,
        tabKeys[3] to supportFragmentManager.findFragmentByTag(tabKeys[3]) as BaseFragment
    )

    override fun setExpanded(isExpanded: Boolean) {
        with(ui.drawerLayout) {
            if (isExpanded) openDrawer(Gravity.START)
            else closeDrawer(Gravity.START)
        }
    }
}