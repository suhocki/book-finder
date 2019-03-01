package app.suhocki.mybooks.ui.base

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.View
import app.suhocki.mybooks.ui.Ids
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.matchParent
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command
import javax.inject.Inject


abstract class FlowFragment : BaseFragment<AnkoComponent<Context>>() {

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    override val ui by lazy {
        Ui(context!!)
    }

    private val currentFragment
        get() = childFragmentManager.findFragmentById(Ids.flowFragmentContainer) as? BaseFragment<*>

    protected val navigator: Navigator by lazy {
        object : SupportAppNavigator(this.activity, childFragmentManager, Ids.flowFragmentContainer) {
            override fun activityBack() {
                onExit()
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

    override fun onBackPressed() {
        currentFragment?.onBackPressed() ?: super.onBackPressed()
    }

    open fun onExit() {}

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        childFragmentManager
            .fragments
            .filter { it.isVisible }
            .forEach { it.userVisibleHint = isVisibleToUser }
    }

    inner class Ui(context: Context) : AnkoComponent<Context> {
        lateinit var parent: View

        init {
            createView(AnkoContext.create(context, context, false))
        }

        override fun createView(ui: AnkoContext<Context>) =
            ui.frameLayout {
                id = Ids.flowFragmentContainer
                lparams(matchParent, matchParent)
            }
    }
}