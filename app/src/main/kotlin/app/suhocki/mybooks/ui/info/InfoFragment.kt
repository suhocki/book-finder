package app.suhocki.mybooks.ui.info

import android.os.Bundle
import android.view.View
import app.suhocki.mybooks.BuildConfig
import app.suhocki.mybooks.data.dialog.DialogManager
import app.suhocki.mybooks.domain.model.Info
import app.suhocki.mybooks.openCaller
import app.suhocki.mybooks.openLink
import app.suhocki.mybooks.openMap
import app.suhocki.mybooks.ui.app.AppView
import app.suhocki.mybooks.ui.base.BaseFragment
import app.suhocki.mybooks.ui.base.eventbus.ShopInfoUpdatedEvent
import app.suhocki.mybooks.ui.base.mpeventbus.MPEventBus
import app.suhocki.mybooks.ui.info.listener.OnInfoClickListener
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.support.v4.email
import toothpick.Toothpick
import javax.inject.Inject


class InfoFragment : BaseFragment<InfoUI>(), InfoView, OnInfoClickListener {

    @InjectPresenter
    lateinit var presenter: InfoPresenter

    @ProvidePresenter
    fun providePresenter(): InfoPresenter =
        scope.getInstance(InfoPresenter::class.java)

    override val ui by lazy { InfoUI() }

    private val adapter by lazy {
        InfoAdapter(
            this,
            presenter::onVersionLongClick
        )
    }

    @Inject
    lateinit var dialogManager: DialogManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toothpick.inject(this, scope)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        ui.toolbar.setNavigationOnClickListener {
        }

        ui.recyclerView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        MPEventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        MPEventBus.getDefault().unregister(this)
    }

    override fun showInfoItems(items: MutableList<Any>) {
        adapter.submitList(items)
    }

    @Suppress("NON_EXHAUSTIVE_WHEN")
    override fun onInfoClick(info: Info) {
        when (info.type) {
            Info.InfoType.PHONE -> context!!.openCaller(info.name)

            Info.InfoType.EMAIL -> email(info.name)

            Info.InfoType.WEBSITE -> context!!.openLink(info.valueForNavigation!!)

            Info.InfoType.FACEBOOK -> context!!.openLink(info.valueForNavigation!!)

            Info.InfoType.VK -> context!!.openLink(info.valueForNavigation!!)

            Info.InfoType.ADDRESS -> context!!.openMap(info.name)

            Info.InfoType.ABOUT_DEVELOPER -> context!!.openLink(BuildConfig.ABOUT_DEVELOPER_URL)

            Info.InfoType.LICENSES -> presenter.onLicensesClick()

            Info.InfoType.CHANGELOG -> presenter.onChangelogClick()
        }
    }

    override fun showDebugPanel(debugEnabled: Boolean) =
        (activity as AppView).showDebugPanel(debugEnabled)

    override fun showProgress(isVisible: Boolean) {
        ui.progressBar.visibility =
                if (isVisible) View.VISIBLE
                else View.GONE
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onShopInfoUpdated(event: ShopInfoUpdatedEvent) {
        presenter.loadData()
    }

    override fun showAppSettingsDialog(
        adminModeEnabled: Boolean,
        debugPanelEnabled: Boolean
    ) = dialogManager.showAppSettingsDialog(
        adminModeEnabled,
        debugPanelEnabled,
        presenter::updateAppSettings
    )

    override fun showAdminMode(enabled: Boolean) {
        TODO("not implemented")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        presenter.onBackPressed()
    }

    companion object {
        fun newInstance() = InfoFragment()
    }
}