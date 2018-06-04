package app.suhocki.mybooks.ui.catalog

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.suhocki.mybooks.*
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.domain.model.*
import app.suhocki.mybooks.ui.base.BaseFragment
import app.suhocki.mybooks.ui.base.listener.NavigationHandler
import app.suhocki.mybooks.ui.base.listener.OnBookClickListener
import app.suhocki.mybooks.ui.base.listener.catalog.OnCategoryClickListener
import app.suhocki.mybooks.ui.base.listener.catalog.OnSearchClickListener
import app.suhocki.mybooks.ui.books.BooksActivity
import app.suhocki.mybooks.ui.details.DetailsActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.bottomPadding
import org.jetbrains.anko.dimenAttr
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.dimen
import org.jetbrains.anko.support.v4.onUiThread
import toothpick.Toothpick
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule


class CatalogFragment : BaseFragment(), CatalogView,
    OnCategoryClickListener,
    OnSearchClickListener, OnBookClickListener {

    private val ui by lazy { CatalogUI<CatalogFragment>() }

    @Inject
    lateinit var search: Search

    private val adapter by lazy { CatalogAdapter(this, this, this, search) }

    @InjectPresenter
    lateinit var presenter: CatalogPresenter

    @ProvidePresenter
    fun providePresenter(): CatalogPresenter =
        Toothpick.openScopes(DI.APP_SCOPE, DI.MAIN_ACTIVITY_SCOPE)
            .getInstance(CatalogPresenter::class.java)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Toothpick.openScopes(DI.APP_SCOPE, DI.MAIN_ACTIVITY_SCOPE).apply {
            Toothpick.inject(this@CatalogFragment, this)
        }
        return ui.createView(AnkoContext.create(ctx, this@CatalogFragment))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ui.recyclerView.adapter = adapter
    }

    override fun showCatalogItems(
        catalogItems: List<Any>,
        itemDecoration: RecyclerView.ItemDecoration?,
        scrollToPosition: Int,
        updateSearchView: Boolean
    ) {
        ui.recyclerView.stopScroll()
        adapter.submitList(catalogItems, onAnimationEnd = {
            with(ui.recyclerView) {
                when (scrollToPosition) {
                    SEARCH_POSITION -> {
                        itemDecoration?.let { showRecyclerDecoration(it) }
                        (layoutManager as LinearLayoutManager)
                            .scrollToPositionWithOffset(SEARCH_POSITION, 0)
                        showKeyboard()
                    }

                    BANNER_POSITION -> {
                        Timer().schedule(200) {
                            onUiThread {
                                itemDecoration?.let { showRecyclerDecoration(it) }
                                bottomPadding = 0
                            }
                        }
                        if (updateSearchView) {
                            showBlankSearch()
                            showKeyboard()
                        }
                        scrollToPosition(BANNER_POSITION)
                    }

                    else -> with(ui.recyclerView) {
                        Timer().schedule(200) {
                            onUiThread {
                                itemDecoration?.let { showRecyclerDecoration(it) }
                                bottomPadding = getRecyclerPadding(this@with, catalogItems)
                            }
                        }
                    }
                }
            }
        })
    }

    private fun getRecyclerPadding(parent: RecyclerView, catalogItems: List<Any>): Int {
        var itemsHeight = 0
        catalogItems.forEach {
            when (it) {
                is Search -> itemsHeight += context!!.dimenAttr(R.attr.actionBarSize)

                is Header -> itemsHeight += context!!.dimenAttr(R.attr.actionBarSize)

                is SearchResult -> itemsHeight += dimen(R.dimen.height_search_result)

                is Category -> itemsHeight += context!!.dimenAttr(R.attr.actionBarSize)
            }
        }
        return if (itemsHeight > parent.height) 0
        else parent.height - itemsHeight
    }

    override fun showSearchMode(expanded: Boolean) {
        ui.recyclerView.stopScroll()
        ui.search.visibility = if (expanded) View.GONE else View.VISIBLE
        ui.close.visibility = if (expanded) View.VISIBLE else View.GONE
        ui.back.visibility = if (expanded) View.VISIBLE else View.GONE
        ui.menu.visibility = if (expanded) View.GONE else View.VISIBLE
        (activity as NavigationHandler).setDrawerEnabled(!expanded)
        (activity as NavigationHandler).setBottomNavigationVisible(!expanded)
        if (!expanded) ui.back.hideKeyboard()
    }

    override fun showBlankSearch() {
        adapter.notifyItemChanged(SEARCH_POSITION)
    }

    override fun showRecyclerDecoration(decoration: RecyclerView.ItemDecoration) {
        ui.recyclerView.apply {
            while (itemDecorationCount > 0) removeItemDecorationAt(0)
            addItemDecoration(decoration)
        }
    }

    override fun onCategoryClick(category: Category) {
        context!!.startActivity<BooksActivity>(ARG_CATEGORY to category)
    }

    override fun onExpandSearchClick() {
        with(ui) {
            setGone(menu, search)
            setVisible(back, close)
        }
        presenter.startSearchMode()
    }

    override fun onCollapseSearchClick(): Boolean {
        val cancelWasVisible = ui.close.visibility == View.VISIBLE
        with(ui) {
            setVisible(menu, search)
            setGone(back, close)
            search.hideKeyboard()
        }
        presenter.stopSearchMode()
        return cancelWasVisible
    }

    override fun onClearSearchClick() {
        presenter.clearSearchQuery()
    }

    override fun onStartSearchClick() {
        ui.search.hideKeyboard()
        presenter.search()
    }

    override fun onBookClick(book: Book) {
        context!!.startActivity<DetailsActivity>(BooksActivity.ARG_BOOK to book)
    }

    companion object {
        const val ARG_CATEGORY = "ARG_CATEGORY"
        const val UNDEFINED_POSITION = -1
        const val BANNER_POSITION = 0
        const val SEARCH_POSITION = 1
        const val CATEGORY_POSITION = 2
        const val SEARCH_RESULT_POSITION = 3

        fun newInstance() = CatalogFragment()
    }
}

