package app.suhocki.mybooks.ui.licenses

import android.os.Bundle
import app.suhocki.mybooks.data.assets.AssetsRepository
import app.suhocki.mybooks.di.module.GsonModule
import app.suhocki.mybooks.domain.repository.LicenseRepository
import app.suhocki.mybooks.openLink
import app.suhocki.mybooks.ui.base.BaseFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import toothpick.Scope
import toothpick.config.Module


class LicensesFragment : BaseFragment<LicensesUI>(), LicensesView {

    @InjectPresenter
    lateinit var presenter: LicensesPresenter

    @ProvidePresenter
    fun providePresenter(): LicensesPresenter =
        scope.getInstance(LicensesPresenter::class.java)

    override val ui by lazy { LicensesUI() }

    private val adapter by lazy {
        LicensesAdapter(
            LicensesAdapter.LicensesDiffCallback()
        ) { context!!.openLink(it.url) }
    }

    override val scopeModuleInstaller = { scope: Scope ->
        scope.installModules(
            GsonModule(),
            object: Module() {
                init {
                    bind(LicenseRepository::class.java)
                        .to(AssetsRepository::class.java)
                        .singletonInScope()
                }
            }
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ui.recyclerView.adapter = adapter
    }

    override fun showData(data: List<Any>) {
        adapter.setData(data)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        presenter.onBackPressed()
    }

//    override fun onLicenseClick(license: License) {
//        openLink(license.url)
//    }
}

