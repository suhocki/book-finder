package app.suhocki.mybooks.ui.drawer.navigation

import android.os.Bundle
import app.suhocki.mybooks.ui.app.AppActivity
import app.suhocki.mybooks.ui.base.BaseFragment
import app.suhocki.mybooks.ui.base.ui.AppSettingsUi
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.jetbrains.anko.customView
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.support.v4.alert
import toothpick.Toothpick

class NavigationDrawerFragment : BaseFragment<NavigationDrawerUI>(), NavigationDrawerView {

    @InjectPresenter
    lateinit var presenter: NavigationDrawerPresenter

    @ProvidePresenter
    fun providePresenter(): NavigationDrawerPresenter =
        scope.getInstance(NavigationDrawerPresenter::class.java)

    override val ui: NavigationDrawerUI by lazy { NavigationDrawerUI() }

    private val adapter by lazy {
        NavigationDrawerAdapter(
            NavigationDrawerAdapter.NavigationDrawerDiffCallback(),
            presenter::onMenuItemClick,
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
            val isAdminModeChecked = ui.adminMode.isChecked
            val isDebugPanelChecked = ui.debugPanel.isChecked
            presenter.applySettings(isAdminModeChecked, isDebugPanelChecked)
            presenter.refreshMenuItems()
            (activity as AppActivity).showDebugPanel(isDebugPanelChecked)
            dialog.dismiss()
        }
    }
}