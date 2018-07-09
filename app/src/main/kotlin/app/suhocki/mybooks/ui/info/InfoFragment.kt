package app.suhocki.mybooks.ui.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.domain.model.Info
import app.suhocki.mybooks.openCaller
import app.suhocki.mybooks.openLink
import app.suhocki.mybooks.openMap
import app.suhocki.mybooks.ui.base.BaseFragment
import app.suhocki.mybooks.ui.info.listener.OnInfoClickListener
import app.suhocki.mybooks.ui.licenses.LicensesActivity
import app.suhocki.mybooks.ui.main.listener.NavigationHandler
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.email
import org.jetbrains.anko.support.v4.startActivity
import toothpick.Toothpick


class InfoFragment : BaseFragment(), InfoView, OnInfoClickListener {

    private val ui by lazy { InfoUI<InfoFragment>() }

    private val adapter by lazy {
        InfoAdapter(this)
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

    override fun showInfoItems(items: MutableList<Any>) {
        adapter.submitList(items)
    }

    override fun onInfoClick(info: Info) {
        when (info.type) {
            Info.InfoType.PHONE -> context!!.openCaller(info.name)

            Info.InfoType.EMAIL -> email(info.name)

            Info.InfoType.WEBSITE -> context!!.openLink(info.valueForNavigation!!)

            Info.InfoType.FACEBOOK -> context!!.openLink(info.valueForNavigation!!)

            Info.InfoType.VK -> context!!.openLink(info.valueForNavigation!!)

            Info.InfoType.WORKING_TIME -> {
            }

            Info.InfoType.ADDRESS -> context!!.openMap(info.name)

            Info.InfoType.ABOUT_DEVELOPER -> context!!.openLink(DEVELOPER_LINK)

            Info.InfoType.LICENSES -> startActivity<LicensesActivity>()
        }
    }

    companion object {
        private const val DEVELOPER_LINK = "https://www.linkedin.com/in/suhocki/"

        fun newInstance() = InfoFragment()
    }
}