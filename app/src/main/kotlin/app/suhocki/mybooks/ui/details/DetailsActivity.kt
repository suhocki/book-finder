package app.suhocki.mybooks.ui.details

import android.os.Bundle
import android.support.annotation.DrawableRes
import android.view.MenuItem
import app.suhocki.mybooks.Analytics
import app.suhocki.mybooks.R
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.di.module.DetailsModule
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.openLink
import app.suhocki.mybooks.ui.books.BooksActivity
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.jetbrains.anko.setContentView
import toothpick.Toothpick
import javax.inject.Inject


class DetailsActivity : MvpAppCompatActivity(), DetailsView {

    @InjectPresenter
    lateinit var presenter: DetailsPresenter

    @Inject
    lateinit var ui: DetailsUI

    @Inject
    lateinit var book: Book

    @ProvidePresenter
    fun providePresenter(): DetailsPresenter =
        Toothpick.openScopes(DI.APP_SCOPE, DI.DETAILS_ACTIVITY_SCOPE)
            .getInstance(DetailsPresenter::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val scope = Toothpick.openScopes(DI.APP_SCOPE, DI.DETAILS_ACTIVITY_SCOPE)
        scope.installModules(
            DetailsModule(intent.getParcelableExtra(BooksActivity.ARG_BOOK))
        )
        Toothpick.inject(this, scope)
        ui.setContentView(this@DetailsActivity)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        ui.fabBuy.setOnClickListener {
            presenter.onBuyClicked()
        }
    }

    override fun showFabDrawableRes(@DrawableRes drawableRes: Int) {
        ui.fabBuy.setImageResource(drawableRes)
    }

    override fun openBookWebsite() {
        ui.fabBuy.setImageResource(R.drawable.ic_buy)
        Analytics.bookAddedToCart(book)
        openLink(book.website)
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

