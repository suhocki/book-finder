package app.suhocki.mybooks.ui.app

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.View
import android.widget.Toast
import app.suhocki.mybooks.R
import app.suhocki.mybooks.Screens
import app.suhocki.mybooks.data.firestore.entity.DebugInfo
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.di.module.UiModule
import app.suhocki.mybooks.model.system.debug.DebugPanelController
import app.suhocki.mybooks.model.system.message.SystemMessage
import app.suhocki.mybooks.model.system.message.SystemMessageNotifier
import app.suhocki.mybooks.model.system.message.SystemMessageType
import app.suhocki.mybooks.presentation.global.GlobalMenuController
import app.suhocki.mybooks.ui.Ids
import app.suhocki.mybooks.ui.base.BaseFragment
import app.suhocki.mybooks.ui.base.MessageDialogFragment
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.textColorResource
import org.jetbrains.anko.textView
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command
import toothpick.Toothpick
import toothpick.config.Module
import javax.inject.Inject

private const val STATE_LAUNCH_FLAG = "state_launch_flag"

class AppActivity : MvpAppCompatActivity(), AppView {

    @InjectPresenter
    lateinit var presenter: AppPresenter

    @ProvidePresenter
    fun providePresenter(): AppPresenter =
        Toothpick.openScopes(DI.UI_SCOPE)
            .getInstance(AppPresenter::class.java)

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var systemMessageNotifier: SystemMessageNotifier

    @Inject
    lateinit var debugPanelController: DebugPanelController

    private val currentFragment: BaseFragment<*>?
        get() = supportFragmentManager.findFragmentById(Ids.appContainer) as? BaseFragment<*>

    private val viewHandler = Handler()
    private val ui = AppUI()

    private val navigator: Navigator =
        object : SupportAppNavigator(this, supportFragmentManager, Ids.appContainer) {
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

    init {
        Toothpick.closeScope(DI.UI_SCOPE)
        Toothpick
            .openScopes(DI.APP_SCOPE, DI.UI_SCOPE)
            .installModules(
                UiModule(),
                object : Module() {
                    init {
                        bind(GlobalMenuController::class.java).toInstance(GlobalMenuController())
                    }
                }
            )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        Toothpick.inject(this, Toothpick.openScope(DI.UI_SCOPE))
        super.onCreate(savedInstanceState)
        ui.setContentView(this)

        if (savedInstanceState == null) {
            router.newRootScreen(Screens.DrawerFlow)
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        subscribeOnSystemMessages()
        navigatorHolder.setNavigator(navigator)
        debugPanelController.eventConsumer = ::showDebugInfo
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        unsubscribeOnSystemMessages()
        debugPanelController.eventConsumer = null
        viewHandler.removeCallbacksAndMessages(null)
        super.onPause()
    }

    override fun onBackPressed() {
        currentFragment?.onBackPressed() ?: super.onBackPressed()
    }

    override fun showDebugPanel(show: Boolean) {
        ui.debugPanel.visibility =
                if (show) View.VISIBLE
                else View.GONE
    }

    private fun showDebugInfo(data: Collection<DebugInfo>) = postViewAction {
        with(ui.debugPanel) {
            removeAllViews()

            data.forEach {
                textView {
                    text = getString(R.string.firestore_connection, it.text, it.count)
                    textColorResource = R.color.colorGreen
                }
            }
        }
    }

    private fun showAlertMessage(message: String) {
        MessageDialogFragment.create(
            message = message
        ).show(supportFragmentManager, null)
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun subscribeOnSystemMessages() {
        systemMessageNotifier.notificationReceiver = { msg: SystemMessage ->
            when (msg.type) {
                SystemMessageType.ALERT -> showAlertMessage(msg.text)
                SystemMessageType.TOAST -> showToastMessage(msg.text)
            }
        }
    }

    private fun unsubscribeOnSystemMessages() {
        systemMessageNotifier.notificationReceiver = null
    }

    private fun postViewAction(action: () -> Unit) {
        viewHandler.post(action)
    }
}