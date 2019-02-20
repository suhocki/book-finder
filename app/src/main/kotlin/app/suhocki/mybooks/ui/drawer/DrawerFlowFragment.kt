package app.suhocki.mybooks.ui.drawer

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import app.suhocki.mybooks.Screens
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.di.module.FlowNavigationModule
import app.suhocki.mybooks.presentation.global.GlobalMenuController
import app.suhocki.mybooks.setLaunchScreen
import app.suhocki.mybooks.ui.Ids
import app.suhocki.mybooks.ui.admin.AdminFlowFragment
import app.suhocki.mybooks.ui.base.BaseFragment
import app.suhocki.mybooks.ui.changelog.ChangelogFragment
import app.suhocki.mybooks.ui.drawer.navigation.NavigationDrawerFragment
import app.suhocki.mybooks.ui.licenses.LicensesFragment
import app.suhocki.mybooks.ui.main.MainFlowFragment
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.terrakok.cicerone.commands.BackTo
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Forward
import toothpick.Scope
import toothpick.Toothpick
import toothpick.config.Module
import javax.inject.Inject

class DrawerFlowFragment : BaseFragment<DrawerFlowUI>() {
    @Inject
    lateinit var menuController: GlobalMenuController

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var router: Router

    private val currentFragment
        get() = childFragmentManager.findFragmentById(Ids.drawerMainContainer) as? BaseFragment<*>
    private val drawerFragment
        get() = childFragmentManager.findFragmentById(Ids.drawerMenuContainer) as? NavigationDrawerFragment

    override val parentScopeName = DI.UI_SCOPE
    override val scopeModuleInstaller = { scope: Scope ->
        scope.installModules(
            FlowNavigationModule(scope.getInstance(Router::class.java)),
            object : Module() {
                init {
                    bind(GlobalMenuController::class.java).toInstance(GlobalMenuController())
                }
            }
        )
    }

    override val ui by lazy { DrawerFlowUI() }

    private val navigator: Navigator by lazy {
        object : SupportAppNavigator(this.activity, childFragmentManager, Ids.drawerMainContainer) {
            override fun applyCommands(commands: Array<out Command>?) {
                super.applyCommands(commands)
                updateNavDrawer()
            }

            override fun activityBack() {
                router.exit()
            }

            override fun setupFragmentTransaction(
                command: Command?,
                currentFragment: Fragment?,
                nextFragment: Fragment?,
                fragmentTransaction: FragmentTransaction
            ) {
                //fix incorrect order lifecycle callback of MainFlowFragment
                fragmentTransaction.setReorderingAllowed(true)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toothpick.inject(this, scope)

        if (childFragmentManager.fragments.isEmpty()) {
            childFragmentManager
                .beginTransaction()
                .replace(Ids.drawerMenuContainer, NavigationDrawerFragment())
                .commitNow()

            navigator.setLaunchScreen(Screens.MainFlow)
        } else {
            updateNavDrawer()
        }
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
        menuController.drawerStateReceiver = ::openNavDrawer
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        menuController.drawerStateReceiver = null
        super.onPause()
    }

    //region nav drawer
    private fun openNavDrawer(open: Boolean) {
        if (open) ui.drawerLayout.openDrawer(GravityCompat.START)
        else ui.drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun updateNavDrawer() {
        childFragmentManager.executePendingTransactions()

        drawerFragment?.let { drawerFragment ->
            currentFragment?.let {
                val menuItemId = when (it) {
                    is MainFlowFragment -> Ids.navCatalog
                    is AdminFlowFragment -> Ids.navAdmin
                    is LicensesFragment -> Ids.navLicenses
                    is ChangelogFragment -> Ids.navChanges
                    else -> Ids.navCatalog
                }
                drawerFragment.presenter.selectMenuItem(menuItemId)
            }
        }
    }
    //endregion

    override fun onBackPressed() {
        if (ui.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            openNavDrawer(false)
        } else {
            currentFragment?.onBackPressed() ?: router.exit()
        }
    }
}