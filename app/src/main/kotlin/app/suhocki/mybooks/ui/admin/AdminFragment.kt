package app.suhocki.mybooks.ui.admin

import android.os.Bundle
import android.os.Environment
import android.support.annotation.StringRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.suhocki.mybooks.R
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.di.module.AdminModule
import app.suhocki.mybooks.di.module.GsonModule
import app.suhocki.mybooks.ui.admin.background.UploadService
import app.suhocki.mybooks.ui.admin.eventbus.ProgressEvent
import app.suhocki.mybooks.ui.admin.eventbus.UploadServiceEvent
import app.suhocki.mybooks.ui.base.BaseFragment
import app.suhocki.mybooks.ui.base.eventbus.ErrorEvent
import app.suhocki.mybooks.ui.main.listener.NavigationHandler
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.startService
import org.jetbrains.anko.textResource
import toothpick.Toothpick

class AdminFragment : BaseFragment(), AdminView {

    @InjectPresenter
    lateinit var presenter: AdminPresenter

    @ProvidePresenter
    fun providePresenter(): AdminPresenter {
        val scope = Toothpick.openScopes(
            DI.APP_SCOPE,
            DI.MAIN_ACTIVITY_SCOPE,
            DI.GSON_SCOPE,
            DI.ADMIN_SCOPE
        )
        val downloadDirectory = requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).path
        val adminModule = AdminModule(requireContext(), downloadDirectory)
        val gsonModule = GsonModule(requireContext())
        scope.installModules(adminModule, gsonModule)

        return scope.getInstance(AdminPresenter::class.java)
    }

    private val ui by lazy {
        AdminUI<AdminFragment> {
            presenter.loadFiles()
        }
    }

    private val adapter by lazy {
        AdminAdapter { startService<UploadService>(UploadService.ARG_FILE to it) }
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
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun showData(data: List<Any>, changedPosition: Int, payload: Any?) {
        adapter.submitList(data, changedPosition, payload)
    }

    override fun showJsonProgress(isVisible: Boolean, progress: Int?) {
        ui.progressBar.visibility =
                if (isVisible) View.VISIBLE
                else View.GONE

        progress?.let {
            ui.progressText.visibility = View.VISIBLE
            ui.progressText.text = getString(R.string.percent, progress)
        } ?: let {
            ui.progressText.visibility = View.INVISIBLE
        }
    }

    override fun showError(@StringRes messageRes: Int?, isVisible: Boolean) {
        if (isVisible) {
            ui.errorText.textResource = messageRes!!
            ui.errorView.visibility = View.VISIBLE
        } else {
            ui.errorView.visibility = View.GONE
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUploadServiceEvent(uploadServiceEvent: UploadServiceEvent) {
        val uploadControl = uploadServiceEvent.uploadControl
        if (uploadControl != null) presenter.insertUploadControl(
            adapter.items,
            uploadControl
        ) else presenter.removeUploadControl(adapter.items)
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onProgressEvent(progressEvent: ProgressEvent) {
        presenter.onDownloadProgress(adapter.items, progressEvent)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onErrorEvent(errorEvent: ErrorEvent) {
        presenter.onError(requireContext(), errorEvent)
    }

    companion object {
        fun newInstance() = AdminFragment()
    }
}