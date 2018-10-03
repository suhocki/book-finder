package app.suhocki.mybooks.ui.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.suhocki.mybooks.BuildConfig
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.domain.model.Info
import app.suhocki.mybooks.openCaller
import app.suhocki.mybooks.openLink
import app.suhocki.mybooks.openMap
import app.suhocki.mybooks.ui.admin.eventbus.DatabaseUpdatedEvent
import app.suhocki.mybooks.ui.base.BaseFragment
import app.suhocki.mybooks.ui.base.listener.AdminModeEnabler
import app.suhocki.mybooks.ui.changelog.ChangelogActivity
import app.suhocki.mybooks.ui.info.listener.OnInfoClickListener
import app.suhocki.mybooks.ui.licenses.LicensesActivity
import app.suhocki.mybooks.ui.main.listener.NavigationHandler
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.email
import org.jetbrains.anko.support.v4.startActivity
import toothpick.Toothpick


class InfoFragment : BaseFragment(), InfoView, OnInfoClickListener {

    private val ui by lazy { InfoUI<InfoFragment>() }

    private val adapter by lazy {
        InfoAdapter(this, presenter::toogleAdminMode)
    }

    @InjectPresenter
    lateinit var presenter: InfoPresenter

    @ProvidePresenter
    fun providePresenter(): InfoPresenter =
        Toothpick.openScopes(DI.APP_SCOPE)
            .getInstance(InfoPresenter::class.java)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = ui.createView(AnkoContext.create(ctx, this@InfoFragment))

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        ui.toolbar.setNavigationOnClickListener {
            (activity as NavigationHandler).setDrawerExpanded(true)
        }

        ui.recyclerView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
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

            Info.InfoType.LICENSES -> startActivity<LicensesActivity>()

            Info.InfoType.CHANGELOG -> startActivity<ChangelogActivity>()
        }
    }

    override fun showAdminMode(enabled: Boolean) =
        (activity as AdminModeEnabler).toogleAdminMode(enabled, true)

    override fun showProgress(isVisible: Boolean) {
        ui.progressBar.visibility =
                if (isVisible) View.VISIBLE
                else View.GONE
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onDatabaseUpdated(event: DatabaseUpdatedEvent) {
        presenter.loadData()
    }


    companion object {
        fun newInstance() = InfoFragment()
    }
}