package app.suhocki.mybooks.ui.base

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import app.suhocki.mybooks.App
import app.suhocki.mybooks.objectScopeName
import com.arellomobile.mvp.MvpAppCompatFragment
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.support.v4.ctx
import toothpick.Scope
import toothpick.Toothpick

private const val PROGRESS_TAG = "bf_progress"
private const val STATE_LAUNCH_FLAG = "state_launch_flag"
private const val STATE_SCOPE_NAME = "state_scope_name"

abstract class BaseFragment<UI: AnkoComponent<Context>> : MvpAppCompatFragment(), AnkoLogger {
    private var instanceStateSaved: Boolean = false

    private val viewHandler = Handler()

    protected open val parentScopeName: String by lazy {
        (parentFragment as? BaseFragment<*>)?.fragmentScopeName
            ?: throw RuntimeException("Must be parent fragment!")
    }

    protected abstract val ui: UI
    protected open val scopeModuleInstaller: (Scope) -> Unit = {}

    private lateinit var fragmentScopeName: String
    protected lateinit var scope: Scope
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        val savedAppCode = savedInstanceState?.getString(STATE_LAUNCH_FLAG)

        //False - if fragment was restored without new app process (for example: activity rotation)
        val isNewInAppProcess = savedAppCode != App.appCode
        fragmentScopeName = savedInstanceState?.getString(STATE_SCOPE_NAME) ?: objectScopeName()
        scope = Toothpick.openScopes(parentScopeName, fragmentScopeName)
            .apply {
                if (isNewInAppProcess) scopeModuleInstaller(this)
            }

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ui.createView(AnkoContext.create(ctx))

    override fun onResume() {
        super.onResume()
        instanceStateSaved = false
    }

    //fix for async views (like swipeToRefresh and RecyclerView)
    //if synchronously call actions on swipeToRefresh in sequence show and hide then swipeToRefresh will not hidden
    protected fun postViewAction(action: () -> Unit) {
        viewHandler.post(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewHandler.removeCallbacksAndMessages(null)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE_LAUNCH_FLAG, App.appCode)
        outState.putString(STATE_SCOPE_NAME, fragmentScopeName)
        instanceStateSaved = true
    }

    override fun onDestroy() {
        super.onDestroy()
        if (activity?.isChangingConfigurations == false) {
            //destroy this fragment with scope
            Toothpick.closeScope(scope.name)
        }
    }

    open fun onBackPressed() {}

}