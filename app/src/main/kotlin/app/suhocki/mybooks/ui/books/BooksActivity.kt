package app.suhocki.mybooks.ui.books

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.ui.books.adapter.BooksAdapter
import app.suhocki.mybooks.ui.books.adapter.OnBookClickListener
import app.suhocki.mybooks.ui.catalog.CatalogFragment
import app.suhocki.mybooks.ui.details.DetailsActivity
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.startActivity
import toothpick.Toothpick
import toothpick.config.Module


class BooksActivity : MvpAppCompatActivity(), BooksView, OnBookClickListener {

    @InjectPresenter
    lateinit var presenter: BooksPresenter

    private val ui by lazy { BooksUI() }

    private val adapter by lazy { BooksAdapter(this) }

    @ProvidePresenter
    fun providePresenter(): BooksPresenter {
        val scopeName = "BooksActivity_${hashCode()}"
        val scope = Toothpick.openScopes(DI.APP_SCOPE, scopeName)
        scope.installModules(object : Module() {
            init {
                bind(Category::class.java)
                    .toInstance(intent.getParcelableExtra(CatalogFragment.ARG_CATEGORY))
            }
        })

        return scope.getInstance(BooksPresenter::class.java).also {
            Toothpick.closeScope(scopeName)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val scope = Toothpick.openScopes(DI.APP_SCOPE, DI.BOOKS_ACTIVITY_SCOPE)
        Toothpick.inject(this@BooksActivity, scope)
        ui.apply {
            setContentView(this@BooksActivity)
            recyclerView.adapter = adapter
        }
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

