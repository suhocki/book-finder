package app.suhocki.mybooks.ui.books

import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import app.suhocki.mybooks.R
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.di.module.BooksModule
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.ui.base.listener.OnBookClickListener
import app.suhocki.mybooks.ui.books.listener.DrawerHandler
import app.suhocki.mybooks.ui.books.listener.OnFilterClickListener
import app.suhocki.mybooks.ui.catalog.CatalogFragment
import app.suhocki.mybooks.ui.details.DetailsActivity
import app.suhocki.mybooks.ui.filter.FilterFragment
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.startActivity
import toothpick.Toothpick


class BooksActivity : MvpAppCompatActivity(), BooksView,
    OnBookClickListener, OnFilterClickListener,
    DrawerHandler {

    @InjectPresenter
    lateinit var presenter: BooksPresenter

    private val ui by lazy { BooksUI() }

    private val adapter by lazy { BooksAdapter(this) }

    @ProvidePresenter
    fun providePresenter(): BooksPresenter {
        val scope = Toothpick.openScopes(DI.APP_SCOPE, DI.BOOKS_ACTIVITY_SCOPE)
        val category = intent.getParcelableExtra<Category>(CatalogFragment.ARG_CATEGORY)
        scope.installModules(BooksModule(category))

        return scope.getInstance(BooksPresenter::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui.apply {
            setContentView(this@BooksActivity)
            recyclerView.adapter = adapter
        }
        initFilter()
    }

    private fun initFilter() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.id_filter_container, FilterFragment.newInstance())
            .commitNow()
        supportFragmentManager.executePendingTransactions()
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

    override fun onFilterClick() {
        setDrawerExpanded(true)
    }

    override fun setDrawerExpanded(isExpanded: Boolean) {
        with(ui.drawerLayout) {
            if (isExpanded) openDrawer(Gravity.END)
            else closeDrawer(Gravity.END)
        }
    }

    override fun onBackPressed() {
        with(ui.drawerLayout) {
            if (isDrawerOpen(Gravity.END)) setDrawerExpanded(false)
            else super.onBackPressed()
        }
    }

    companion object {
        const val ARG_BOOK = "ARG_BOOK"
    }
}

