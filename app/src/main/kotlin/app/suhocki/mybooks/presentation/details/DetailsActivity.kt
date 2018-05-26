package app.suhocki.mybooks.presentation.details

import android.os.Bundle
import android.view.MenuItem
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.presentation.books.BooksActivity
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.jetbrains.anko.setContentView
import toothpick.Toothpick


class DetailsActivity : MvpAppCompatActivity(), DetailsView {

    @InjectPresenter
    lateinit var presenter: DetailsPresenter

    private var layout = DetailsUI()

    @ProvidePresenter
    fun providePresenter(): DetailsPresenter =
        Toothpick.openScopes(DI.APP_SCOPE, DI.DETAILS_ACTIVITY_SCOPE)
            .getInstance(DetailsPresenter::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val scope = Toothpick.openScopes(DI.APP_SCOPE, DI.DETAILS_ACTIVITY_SCOPE)
        Toothpick.inject(this@DetailsActivity, scope)
        layout.apply {
            book = intent.getParcelableExtra(BooksActivity.ARG_BOOK)
            setContentView(this@DetailsActivity)
        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) Toothpick.closeScope(DI.DETAILS_ACTIVITY_SCOPE)
    }
}

