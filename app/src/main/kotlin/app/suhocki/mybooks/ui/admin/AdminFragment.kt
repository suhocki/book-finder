package app.suhocki.mybooks.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.di.module.AdminModule
import app.suhocki.mybooks.di.module.GsonModule
import app.suhocki.mybooks.ui.base.BaseFragment
import app.suhocki.mybooks.ui.main.listener.NavigationHandler
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.support.v4.ctx
import toothpick.Toothpick

class AdminFragment : BaseFragment(), AdminView {

    @InjectPresenter
    lateinit var presenter: AdminPresenter

    @ProvidePresenter
    fun providePresenter(): AdminPresenter {
        val scope = Toothpick.openScopes(
            DI.APP_SCOPE,
            DI.MAIN_ACTIVITY_SCOPE,
            DI.GSON_SCOPE
        )
        val adminModule = AdminModule()
        val gsonModule = GsonModule(context!!)
        scope.installModules(adminModule, gsonModule)

        return scope.getInstance(AdminPresenter::class.java)

    }

    private val ui by lazy { AdminUI<AdminFragment>() }

    private val adapter by lazy { AdminAdapter() }

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

    override fun showData(data: List<Any>) {
        adapter.submitList(data)
    }


    companion object {
        fun newInstance() = AdminFragment()
    }
}