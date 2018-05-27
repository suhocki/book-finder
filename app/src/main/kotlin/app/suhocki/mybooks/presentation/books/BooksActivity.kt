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
import app.suhocki.mybooks.presentation.catalog.CatalogFragment
import app.suhocki.mybooks.presentation.details.DetailsActivity
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.startActivity
import toothpick.Toothpick


class BooksActivity : MvpAppCompatActivity(), BooksView, OnBookClickListener {

    @InjectPresenter
    lateinit var presenter: BooksPresenter

    private val ui = BooksUI()

    private val adapter by lazy { BooksAdapter() }

    @ProvidePresenter
    fun providePresenter(): BooksPresenter =
        Toothpick.openScopes(DI.APP_SCOPE, DI.BOOKS_ACTIVITY_SCOPE)
            .apply {
                val category = intent.getParcelableExtra<Category>(CatalogFragment.ARG_CATEGORY)
                installModules(BooksActivityModule(category))
            }.getInstance(BooksPresenter::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val scope = Toothpick.openScopes(DI.APP_SCOPE, DI.BOOKS_ACTIVITY_SCOPE)
        Toothpick.inject(this@BooksActivity, scope)
        ui.apply {
            setContentView(this@BooksActivity)
            recyclerView.adapter = adapter
        }
        adapter.setOnBookClickListener(this)
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
        ui.emptyView.visibility = View.VISIBLE
    }

    override fun showProgressVisible(visible: Boolean) {
        ui.progressBar.visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun onBookClick(book: Book) {
        startActivity<DetailsActivity>(ARG_BOOK to book)
    }

    companion object {
        const val ARG_BOOK = "ARG_BOOK"
    }
}

