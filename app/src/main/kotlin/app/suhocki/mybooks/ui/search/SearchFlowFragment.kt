package app.suhocki.mybooks.ui.search

import android.os.Bundle
import app.suhocki.mybooks.Screens
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.di.module.FlowNavigationModule
import app.suhocki.mybooks.hideKeyboard
import app.suhocki.mybooks.setLaunchScreen
import app.suhocki.mybooks.ui.base.FlowFragment
import ru.terrakok.cicerone.Router
import toothpick.Scope
import toothpick.Toothpick
import javax.inject.Inject

class SearchFlowFragment : FlowFragment() {

    override val parentScopeName = DI.UI_SCOPE

    @Inject
    lateinit var router: Router

    override val scopeModuleInstaller = { scope: Scope ->
        scope.installModules(
            FlowNavigationModule(scope.getInstance(Router::class.java))
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toothpick.inject(this, scope)
        if (childFragmentManager.fragments.isEmpty()) {
            navigator.setLaunchScreen(Screens.Search)
        }
    }

    override fun onExit() {
        activity?.hideKeyboard()
        router.exit()
    }
}