package app.suhocki.mybooks.ui.books

import android.arch.persistence.db.SupportSQLiteQuery
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v4.widget.DrawerLayout
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import app.suhocki.mybooks.Analytics
import app.suhocki.mybooks.R
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.di.module.BooksModule
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.openLink
import app.suhocki.mybooks.ui.Ids
import app.suhocki.mybooks.ui.base.entity.BookEntity
import app.suhocki.mybooks.ui.base.listener.OnBookClickListener
import app.suhocki.mybooks.ui.base.listener.OnFilterResultListener
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
    OnBookClickListener, OnFilterClickListener, OnFilterResultListener {

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
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
                override fun onDrawerClosed(drawerView: View) {
                    presenter.setDrawerExpanded(false)
                }

                override fun onDrawerStateChanged(newState: Int) {}
                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
                override fun onDrawerOpened(drawerView: View) {}
            })
        }
        if (savedInstanceState == null) {
            initFilter()
        }
    }

    private fun initFilter() {
        supportFragmentManager
            .beginTransaction()
            .replace(Ids.filterContainer, FilterFragment.newInstance())
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

    override fun showBooks(books: List<Book>, scrollToTop: Boolean) {
        adapter.submitList(books, onAnimationEnd = {
            if (scrollToTop) ui.recyclerView.scrollToPosition(0)
            presenter.setProgressInvisible()
        })
    }

    override fun showEmptyScreen(isEmpty: Boolean) {
        with(ui) {
            if (isEmpty) {
                recyclerView.visibility = View.GONE
                emptyView.visibility = View.VISIBLE
            } else {
                recyclerView.visibility = View.VISIBLE
                emptyView.visibility = View.GONE
            }
        }
    }

    override fun showProgressVisible(visible: Boolean) {
        ui.progressBar.visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun onBookClick(book: Book) {
        startActivity<DetailsActivity>(ARG_BOOK to book)
    }

    override fun onBuyBookClick(book: BookEntity) {
        presenter.onBuyBookClicked(book)
    }

    override fun showBuyDrawableForItem(book: BookEntity, @DrawableRes drawableRes: Int) {
        val indexOfBook = adapter.items.indexOf(book)
        adapter.notifyItemChanged(indexOfBook, drawableRes)
    }

    override fun openBookWebsite(book: Book) {
        Analytics.bookAddedToCart(book)
        openLink(book.website)
    }

    override fun onFilterClick() {
        presenter.setDrawerExpanded(true)
    }

    override fun onFilterCollapseClick() {
        presenter.setDrawerExpanded(false)
    }

    override fun showDrawerExpanded(isExpanded: Boolean) {
        with(ui.drawerLayout) {
            if (isExpanded) openDrawer(Gravity.END)
            else closeDrawer(Gravity.END)
        }
    }

    override fun onBackPressed() {
        with(ui.drawerLayout) {
            if (isDrawerOpen(Gravity.END)) presenter.setDrawerExpanded(false)
            else super.onBackPressed()
        }
    }

    override fun onFilterResult(searchQuery: SupportSQLiteQuery) {
        presenter.applyFilter(searchQuery)
        presenter.setDrawerExpanded(false)
    }

    override fun onFilterReset() {
        presenter.resetFilter()
    }

    companion object {
        const val ARG_BOOK = "ARG_BOOK"
    }
}

