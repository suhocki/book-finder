package app.suhocki.mybooks.ui.changelog

import android.os.Bundle
import android.view.MenuItem
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.di.module.ChangelogModule
import app.suhocki.mybooks.di.module.GsonModule
import app.suhocki.mybooks.openLink
import app.suhocki.mybooks.ui.changelog.listener.OnDownloadFileClickListener
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.jetbrains.anko.setContentView
import toothpick.Toothpick

class ChangelogActivity : MvpAppCompatActivity(), ChangelogView, OnDownloadFileClickListener {

    @InjectPresenter
    lateinit var presenter: ChangelogPresenter

    private val ui by lazy { ChangelogUI() }

    private val adapter by lazy { ChangelogAdapter(this) }

    @ProvidePresenter
    fun providePresenter(): ChangelogPresenter {
        val scope = Toothpick.openScopes(
            DI.APP_SCOPE,
            DI.GSON_SCOPE,
            DI.CHANGELOG_ACTIVITY_SCOPE
        )
        scope.installModules(GsonModule(this), ChangelogModule())

        return scope.getInstance(ChangelogPresenter::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui.apply {
            setContentView(this@ChangelogActivity)
            recyclerView.adapter = adapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) Toothpick.closeScope(DI.CHANGELOG_ACTIVITY_SCOPE)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

    override fun showChangelog(data: List<Any>) {
        adapter.submitList(data)
    }

    override fun onDownloadFile(url: String) {
        openLink(url)
    }
}