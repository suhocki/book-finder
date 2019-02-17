package app.suhocki.mybooks.ui.admin

import android.os.Bundle
import android.support.annotation.StringRes
import android.view.View
import app.suhocki.mybooks.data.api.GoogleDriveApi
import app.suhocki.mybooks.data.dialog.DialogManager
import app.suhocki.mybooks.di.module.GsonModule
import app.suhocki.mybooks.di.provider.GoogleDriveApiProvider
import app.suhocki.mybooks.domain.model.admin.UploadControl
import app.suhocki.mybooks.ui.admin.eventbus.UploadCompleteEvent
import app.suhocki.mybooks.ui.base.BaseFragment
import app.suhocki.mybooks.ui.base.eventbus.ErrorEvent
import app.suhocki.mybooks.ui.base.mpeventbus.MPEventBus
import app.suhocki.mybooks.ui.app.listener.NavigationHandler
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import okhttp3.OkHttpClient
import app.suhocki.mybooks.di.provider.GoogleDriveOkHttpProvider
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.textResource
import toothpick.Scope
import toothpick.Toothpick
import toothpick.config.Module
import javax.inject.Inject

class AdminFragment : BaseFragment<AdminUI>(), AdminView {

    @InjectPresenter
    lateinit var presenter: AdminPresenter

    @ProvidePresenter
    fun providePresenter(): AdminPresenter =
        scope.getInstance(AdminPresenter::class.java)

    override val scopeModuleInstaller = { scope: Scope ->
        scope.installModules(
            GsonModule(),
            object : Module() {
                init {
                    bind(OkHttpClient::class.java)
                        .toProvider(GoogleDriveOkHttpProvider::class.java)
                        .providesSingletonInScope()

                    bind(GoogleDriveApi::class.java)
                        .toProvider(GoogleDriveApiProvider::class.java)
                        .providesSingletonInScope()
                }
            }
        )
    }

    @Inject
    lateinit var dialogManager: DialogManager

    override val ui by lazy {
        AdminUI {
            presenter.loadFiles()
        }
    }

    private val adapter: AdminAdapter by lazy {
        AdminAdapter(
            { presenter.upload(it, adapter.items) },
            { presenter.stopUpload(adapter.items, true) }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toothpick.inject(this, scope)
    }

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
    fun onUploadCompleteEvent(uploadCompleteEvent: UploadCompleteEvent) {
        presenter.stopUpload(adapter.items, uploadCompleteEvent.shouldKillService)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onErrorEvent(errorEvent: ErrorEvent) {
        presenter.onError(errorEvent)
    }

    companion object {
        fun newInstance() = AdminFragment()
    }
}