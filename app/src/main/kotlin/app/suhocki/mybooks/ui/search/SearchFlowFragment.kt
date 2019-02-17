package app.suhocki.mybooks.ui.search

import android.os.Bundle
import app.suhocki.mybooks.Screens
import app.suhocki.mybooks.hideKeyboard
import app.suhocki.mybooks.setLaunchScreen
import app.suhocki.mybooks.ui.base.FlowFragment
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import javax.inject.Inject

class SearchFlowFragment : FlowFragment() {

    @Inject
    lateinit var router: Router

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