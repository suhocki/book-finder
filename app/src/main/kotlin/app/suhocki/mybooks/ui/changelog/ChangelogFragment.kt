package app.suhocki.mybooks.ui.changelog

import android.os.Bundle
import app.suhocki.mybooks.data.assets.AssetsRepository
import app.suhocki.mybooks.di.module.GsonModule
import app.suhocki.mybooks.domain.repository.ChangelogRepository
import app.suhocki.mybooks.openLink
import app.suhocki.mybooks.ui.base.BaseFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import toothpick.Scope
import toothpick.Toothpick
import toothpick.config.Module

class ChangelogFragment : BaseFragment<ChangelogUI>(), ChangelogView {

    @InjectPresenter
    lateinit var presenter: ChangelogPresenter

    @ProvidePresenter
    fun providePresenter(): ChangelogPresenter =
        scope.getInstance(ChangelogPresenter::class.java)

    override val ui by lazy { ChangelogUI() }

    override val scopeModuleInstaller = { scope: Scope ->
        scope.installModules(
            GsonModule(),
            object: Module() {
                init {
                    bind(ChangelogRepository::class.java)
                        .to(AssetsRepository::class.java)
                        .singletonInScope()
                }
            }
        )
    }

    private val adapter by lazy {
        ChangelogAdapter(
            ChangelogAdapter.ChangelogDiffCallback()
        ) { context!!.openLink(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toothpick.inject(this, scope)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ui.recyclerView.adapter = adapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
        presenter.onBackPressed()
    }

    override fun showData(data: List<Any>) {
        adapter.setData(data)
    }
}