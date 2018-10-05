package app.suhocki.mybooks.ui.licenses

import android.os.Bundle
import android.view.MenuItem
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.di.module.GsonModule
import app.suhocki.mybooks.di.module.LicensesModule
import app.suhocki.mybooks.domain.model.License
import app.suhocki.mybooks.openLink
import app.suhocki.mybooks.ui.licenses.listener.OnLicenseClickListener
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.jetbrains.anko.setContentView
import toothpick.Toothpick


class LicensesActivity : MvpAppCompatActivity(), LicensesView, OnLicenseClickListener {

    @InjectPresenter
    lateinit var presenter: LicensesPresenter

    private val ui by lazy { LicensesUI() }

    private val adapter by lazy { LicensesAdapter(this) }

    @ProvidePresenter
    fun providePresenter(): LicensesPresenter {
        val scope = Toothpick.openScopes(
            DI.APP_SCOPE,
            DI.MAIN_ACTIVITY_SCOPE,
            DI.GSON_SCOPE,
            DI.LICENSES_ACTIVITY_SCOPE
        )
        scope.installModules(GsonModule(), LicensesModule())

        return scope.getInstance(LicensesPresenter::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui.apply {
            setContentView(this@LicensesActivity)
            recyclerView.adapter = adapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) Toothpick.closeScope(DI.LICENSES_ACTIVITY_SCOPE)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

    override fun showLicenses(data: List<Any>) {
        adapter.submitList(data)
    }

    override fun onLicenseClick(license: License) {
        openLink(license.url)
    }
}

