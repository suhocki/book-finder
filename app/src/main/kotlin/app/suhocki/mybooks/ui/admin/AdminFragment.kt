package app.suhocki.mybooks.ui.admin

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.di.module.AdminModule
import app.suhocki.mybooks.di.module.GsonModule
import app.suhocki.mybooks.ui.admin.background.UploadService
import app.suhocki.mybooks.ui.admin.eventbus.ProgressEvent
import app.suhocki.mybooks.ui.admin.eventbus.UploadServiceEvent
import app.suhocki.mybooks.ui.base.BaseFragment
import app.suhocki.mybooks.ui.main.listener.NavigationHandler
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.startService
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
        val downloadDirectory = context!!.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).path
        val adminModule = AdminModule(downloadDirectory)
        val gsonModule = GsonModule(context!!)
        scope.installModules(adminModule, gsonModule)

        return scope.getInstance(AdminPresenter::class.java)
    }

    private val ui by lazy { AdminUI<AdminFragment>() }

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

    override fun showData(data: List<Any>, withAnimation: Boolean) {
        adapter.submitList(data, withAnimation)
    }

    override fun showProgress(isVisible: Boolean) {
        ui.progressBar.visibility =
                if (isVisible) View.VISIBLE
                else View.GONE
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
        presenter.showProgress(adapter.items, progressEvent)
    }

    companion object {
        fun newInstance() = AdminFragment()
    }
}