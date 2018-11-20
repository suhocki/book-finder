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
import com.stfalcon.frescoimageviewer.ImageViewer
import org.jetbrains.anko.setContentView
import toothpick.Toothpick


class DetailsActivity : MvpAppCompatActivity(), DetailsView {

    @InjectPresenter
    lateinit var presenter: DetailsPresenter

    lateinit var ui: DetailsUI

    @ProvidePresenter
    fun providePresenter(): DetailsPresenter =
        Toothpick.openScopes(DI.APP_SCOPE, DI.DETAILS_ACTIVITY_SCOPE)
            .apply {
                val bookId = intent.getStringExtra(BooksActivity.ARG_BOOK_ID)
                installModules(DetailsModule(bookId))
            }
            .getInstance(DetailsPresenter::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val scope = Toothpick.openScopes(DI.APP_SCOPE, DI.DETAILS_ACTIVITY_SCOPE)
        Toothpick.inject(this, scope)
    }

    override fun showBook(book: Book) {
        ui = DetailsUI(book)

        ui.setContentView(this@DetailsActivity)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        ui.fabBuy.setOnClickListener {
            presenter.onBuyBookClicked()
        }

        ui.image.setOnClickListener {
            ImageViewer.Builder(this, listOf(book.productLink))
                .hideStatusBar(false)
                .show()
        }
    }

    override fun showFabDrawableRes(@DrawableRes drawableRes: Int) {
        ui.fabBuy.setImageResource(drawableRes)
    }

    override fun openBookWebsite(book: Book) {
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

