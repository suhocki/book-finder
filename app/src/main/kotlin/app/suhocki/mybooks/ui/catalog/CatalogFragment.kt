package app.suhocki.mybooks.ui.catalog

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.suhocki.mybooks.*
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.model.Search
import app.suhocki.mybooks.ui.base.BaseFragment
import app.suhocki.mybooks.ui.base.listener.NavigationHandler
import app.suhocki.mybooks.ui.base.listener.OnBookClickListener
import app.suhocki.mybooks.ui.base.listener.OnCategoryClickListener
import app.suhocki.mybooks.ui.base.listener.OnSearchClickListener
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
    OnCategoryClickListener, OnSearchClickListener, OnBookClickListener {

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
        scrollToPosition: Int
    ) = adapter.submitList(catalogItems, onAnimationEnd = {
        if (scrollToPosition != UNDEFINED_POSITION) with(ui.recyclerView) {
            when (scrollToPosition) {
                SEARCH_POSITION -> {
                    bottomPadding = getRecyclerPaddingToMoveSearchToTop(this, 0)
                    (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                        SEARCH_POSITION,
                        0
                    )
                    showKeyboard()
                }

                BANNER_POSITION -> {
                    Timer().schedule(200) { onUiThread { bottomPadding = 0 } }
                    scrollToPosition(BANNER_POSITION)
                }
            }
        }
    })

    private fun getRecyclerPaddingToMoveSearchToTop(parent: RecyclerView, itemCount: Int): Int {
        var topOffset = parent.height - visibleChildHeightWithFooter(parent, itemCount)
        topOffset -= context!!.dimenAttr(R.attr.actionBarSize) * 2
        return if (topOffset < 0) 0 else topOffset
    }

    private fun visibleChildHeightWithFooter(parent: RecyclerView, itemCount: Int): Int {
        var totalHeight = 0
        var linesCount = Math.min(parent.childCount, itemCount)
        if (parent.layoutManager is GridLayoutManager) {
            linesCount = Math.ceil(
                (linesCount - 1).toDouble() / dimen(R.dimen.height_catalog_decorator)
            ).toInt()
        }
        for (i in 0 until linesCount) {
            totalHeight += parent.getChildAt(i).height + 1
        }
        return totalHeight
    }

    override fun showSearchMode(expanded: Boolean) {
        ui.recyclerView.stopScroll()
        ui.search.visibility = if (expanded) View.GONE else View.VISIBLE
        ui.close.visibility = if (expanded) View.VISIBLE else View.GONE
        ui.back.visibility = if (expanded) View.VISIBLE else View.GONE
        ui.menu.visibility = if (expanded) View.GONE else View.VISIBLE
        (activity as NavigationHandler).setDrawerEnabled(!expanded)
        (activity as NavigationHandler).setBottomNavigationVisible(!expanded)
        if(!expanded) ui.back.hideKeyboard()
    }

    override fun showBlankSearch() {
        adapter.notifyItemChanged(SEARCH_POSITION)
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

        fun newInstance() = CatalogFragment()
    }
}

