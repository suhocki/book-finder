package app.suhocki.mybooks.presentation.details

import android.os.Bundle
import android.view.MenuItem
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.jetbrains.anko.setContentView
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.di.module.DetailsActivityModule
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.presentation.books.BooksActivity
import toothpick.Toothpick
import javax.inject.Inject


class DetailsActivity : MvpAppCompatActivity(), DetailsView {

    @InjectPresenter
    lateinit var presenter: DetailsPresenter

    @Inject
    lateinit var layout: DetailsUI

    @ProvidePresenter
    fun providePresenter(): DetailsPresenter =
        Toothpick.openScopes(DI.APP_SCOPE, DI.DETAILS_ACTIVITY_SCOPE)
            .apply {
                val book = intent.getParcelableExtra<Book>(BooksActivity.ARG_BOOK)
                installModules(DetailsActivityModule(book))
            }.getInstance(DetailsPresenter::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val scope = Toothpick.openScopes(DI.APP_SCOPE, DI.DETAILS_ACTIVITY_SCOPE)
        Toothpick.inject(this@DetailsActivity, scope)
        layout.setContentView(this)
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

    override fun showBookDetails(book: Book) = with(layout) {
        supportActionBar!!.title = book.shortName
    }
}
