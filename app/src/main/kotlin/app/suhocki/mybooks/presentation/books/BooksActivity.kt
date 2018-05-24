package app.suhocki.mybooks.presentation.books

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.di.module.BooksActivityModule
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.presentation.books.adapter.BooksAdapter
import app.suhocki.mybooks.presentation.books.adapter.OnBookClickListener
import app.suhocki.mybooks.presentation.catalog.CatalogActivity
import app.suhocki.mybooks.presentation.details.DetailsActivity
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.startActivity
import toothpick.Toothpick
import javax.inject.Inject


class BooksActivity : MvpAppCompatActivity(), BooksView, OnBookClickListener {

    @InjectPresenter
    lateinit var presenter: BooksPresenter

    @Inject
    lateinit var layout: BooksUI

    @Inject
    lateinit var adapter: BooksAdapter

    @ProvidePresenter
    fun providePresenter(): BooksPresenter =
        Toothpick.openScopes(DI.APP_SCOPE, DI.BOOKS_ACTIVITY_SCOPE)
            .apply {
                val category = intent.getParcelableExtra<Category>(CatalogActivity.ARG_CATEGORY)
                installModules(BooksActivityModule(category))
            }.getInstance(BooksPresenter::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val scope = Toothpick.openScopes(DI.APP_SCOPE, DI.BOOKS_ACTIVITY_SCOPE)
        Toothpick.inject(this@BooksActivity, scope)
        layout.setContentView(this)
        setSupportActionBar(layout.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onStart() {
        super.onStart()
        adapter.setOnBookClickListener(this)
    }

    override fun onStop() {
        super.onStop()
        adapter.setOnBookClickListener(null)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) Toothpick.closeScope(DI.BOOKS_ACTIVITY_SCOPE)
    }

    override fun showTitle(title: String) {
        this.title = title
    }

    override fun showBooks(books: List<Book>) {
        adapter.submitList(books)
    }

    override fun showEmptyScreen() {
        layout.emptyView.visibility = View.VISIBLE
    }

    override fun showProgressVisible(visible: Boolean) {
        layout.progressBar.visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun onBookClick(book: Book) {
        startActivity<DetailsActivity>(ARG_BOOK to book)
    }

    companion object {
        const val ARG_BOOK = "ARG_BOOK"
    }
}

