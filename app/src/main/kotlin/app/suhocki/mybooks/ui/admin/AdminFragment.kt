package app.suhocki.mybooks.ui.admin

import android.os.Bundle
import android.support.annotation.StringRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.suhocki.mybooks.data.dialog.DialogManager
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.di.module.AdminModule
import app.suhocki.mybooks.di.module.GsonModule
import app.suhocki.mybooks.domain.model.admin.UploadControl
import app.suhocki.mybooks.ui.admin.eventbus.ServiceKilledEvent
import app.suhocki.mybooks.ui.base.BaseFragment
import app.suhocki.mybooks.ui.base.eventbus.ErrorEvent
import app.suhocki.mybooks.ui.base.mpeventbus.MPEventBus
import app.suhocki.mybooks.ui.main.listener.NavigationHandler
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.textResource
import toothpick.Toothpick
import javax.inject.Inject

class AdminFragment : BaseFragment(), AdminView {

    @InjectPresenter
    lateinit var presenter: AdminPresenter

    @Inject
    lateinit var dialogManager: DialogManager

    @ProvidePresenter
    fun providePresenter(): AdminPresenter {
        val scope = Toothpick.openScopes(
            DI.APP_SCOPE,
            DI.MAIN_ACTIVITY_SCOPE,
            DI.GSON_SCOPE,
            DI.ADMIN_SCOPE
        )
        scope.installModules(GsonModule(), AdminModule())

        Toothpick.inject(this, scope)
        return scope.getInstance(AdminPresenter::class.java)
    }

    private val ui by lazy {
        AdminUI<AdminFragment> {
            presenter.loadFiles()
        }
    }

    private val adapter: AdminAdapter by lazy {
        AdminAdapter(
            { presenter.upload(it, adapter.items) },
            { presenter.stopUpload(adapter.items, true) }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ui.createView(AnkoContext.create(ctx, this@AdminFragment))

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        ui.toolbar.setNavigationOnClickListener {
            (activity as NavigationHandler).setDrawerExpanded(true)
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

    override fun showData(data: List<Any>) {
        adapter.submitList(data) {
            ui.recyclerView.invalidateItemDecorations()
        }
    }

    override fun showProgress(isVisible: Boolean) {
        ui.progressBar.visibility =
                if (isVisible) View.VISIBLE
                else View.GONE
    }

    override fun showError(@StringRes messageRes: Int?, isVisible: Boolean) {
        if (isVisible) {
            ui.errorText.textResource = messageRes!!
            ui.errorView.visibility = View.VISIBLE
        } else {
            ui.errorView.visibility = View.GONE
        }
    }

    override fun showErrorDialog(messageRes: Int) {
        dialogManager.showErrorDialog(messageRes) {
            presenter.loadFiles()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUploadControlEvent(uploadControl: UploadControl) {
        presenter.insertUploadControl(adapter.items, uploadControl)
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onServiceKilledEvent(serviceKilledEvent: ServiceKilledEvent) {
        presenter.stopUpload(adapter.items, serviceKilledEvent.shouldKillService)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onErrorEvent(errorEvent: ErrorEvent) {
        presenter.onError(errorEvent)
    }

    companion object {
        fun newInstance() = AdminFragment()
    }
}