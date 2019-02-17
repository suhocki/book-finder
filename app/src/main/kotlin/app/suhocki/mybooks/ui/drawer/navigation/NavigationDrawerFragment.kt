package app.suhocki.mybooks.ui.drawer.navigation

import android.os.Bundle
import android.support.annotation.IdRes
import app.suhocki.mybooks.presentation.global.GlobalAppSettingsController
import app.suhocki.mybooks.presentation.global.GlobalMenuController
import app.suhocki.mybooks.ui.base.BaseFragment
import app.suhocki.mybooks.ui.base.ui.AppSettingsUi
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.jetbrains.anko.customView
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.support.v4.alert
import toothpick.Toothpick
import javax.inject.Inject

class NavigationDrawerFragment : BaseFragment<NavigationDrawerUI>(), NavigationDrawerView {

    @InjectPresenter
    lateinit var presenter: NavigationDrawerPresenter

    @ProvidePresenter
    fun providePresenter(): NavigationDrawerPresenter =
        scope.getInstance(NavigationDrawerPresenter::class.java)

    @Inject
    lateinit var appSettingsController: GlobalAppSettingsController

    @Inject
    lateinit var menuController: GlobalMenuController

    override val ui: NavigationDrawerUI by lazy { NavigationDrawerUI() }

    private val adapter by lazy {
        NavigationDrawerAdapter(
            NavigationDrawerAdapter.NavigationDrawerDiffCallback(),
            menuController::selectMenuItem,
            presenter::openAppSettings
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toothpick.inject(this, scope)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ui.recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        menuController.onMenuItemSelectedListeners.add(::selectMenuItem)
        menuController.onAdminModeChangedReceivers.add(presenter::onAdminModeStateChanged)
    }

    override fun onPause() {
        super.onPause()
        menuController.onMenuItemSelectedListeners.remove(::selectMenuItem)
        menuController.onAdminModeChangedReceivers.remove(presenter::onAdminModeStateChanged)
    }

    override fun showData(data: List<Any>) {
        adapter.setData(data)
    }

    override fun showAppSettings(
        adminEnabled: Boolean,
        debugEnabled: Boolean
    ) {
        val ui = AppSettingsUi()

        val dialog =
            alert {
                customView {
                    ui.createView(this)
                }
            }.show()

        ui.adminMode.isChecked = adminEnabled
        ui.debugPanel.isChecked = debugEnabled
        ui.apply.onClick {
            val adminMode = ui.adminMode.isChecked
            val debugPanel = ui.debugPanel.isChecked

            presenter.applySettings(adminMode, debugPanel)
            appSettingsController.applySettings(debugPanel, adminMode)
            menuController.notifyAdminModeChanged()
            dialog.dismiss()
        }
    }

    private fun selectMenuItem(@IdRes menuItemId: Int) {
        presenter.selectMenuItem(menuItemId)
        menuController.close()
    }
}