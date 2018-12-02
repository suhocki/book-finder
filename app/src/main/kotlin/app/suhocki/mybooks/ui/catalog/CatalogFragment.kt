package app.suhocki.mybooks.ui.catalog

import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v7.graphics.drawable.DrawerArrowDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.suhocki.mybooks.*
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.di.module.CatalogModule
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.domain.model.Search
import app.suhocki.mybooks.ui.base.BaseFragment
import app.suhocki.mybooks.ui.base.entity.UiBook
import app.suhocki.mybooks.ui.base.entity.UiItem
import app.suhocki.mybooks.ui.base.eventbus.CatalogItemsUpdatedEvent
import app.suhocki.mybooks.ui.base.listener.OnBookClickListener
import app.suhocki.mybooks.ui.base.listener.OnSearchClickListener
import app.suhocki.mybooks.ui.base.listener.OnSearchListener
import app.suhocki.mybooks.ui.base.mpeventbus.MPEventBus
import app.suhocki.mybooks.ui.books.BooksActivity
import app.suhocki.mybooks.ui.details.DetailsActivity
import app.suhocki.mybooks.ui.main.listener.NavigationHandler
import com.arellomobile.mvp.MoxyReflector
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.dimen
import org.jetbrains.anko.padding
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.*
import toothpick.Toothpick


class CatalogFragment : BaseFragment(), CatalogView,
    OnSearchClickListener, OnBookClickListener, OnSearchListener {

    private val scope by lazy { Toothpick.openScopes(DI.APP_SCOPE, DI.CATALOG_SCOPE) }
    private val ui by lazy { CatalogUI<CatalogFragment>() }

    private val adapter by lazy {
        CatalogAdapter(
            { presenter.loadNextPage() },
            { context!!.startActivity<BooksActivity>(ARG_CATEGORY_ID to it) },
            this,
            this,
            this
        )
    }

    @InjectPresenter
    lateinit var presenter: CatalogPresenter

    @ProvidePresenter
    fun providePresenter(): CatalogPresenter {
        val isSearchMode = arguments!!.getBoolean(ARG_IS_SEARCH_MODE)
        val dividerOffset = dimen(R.dimen.height_divider_decorator)
        val searchOffset = dip(4)
        val viewState = MoxyReflector.getViewState(CatalogPresenter::class.java)
        val catalogModule = CatalogModule(isSearchMode, dividerOffset, searchOffset, viewState)

        scope.installModules(catalogModule)
        val catalogPresenter = scope.getInstance(CatalogPresenter::class.java)
        Toothpick.closeScope(DI.CATALOG_SCOPE)

        return catalogPresenter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ui.createView(AnkoContext.create(ctx, this@CatalogFragment))

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        ui.toolbar.setNavigationOnClickListener {
            val animatedDrawable = ui.toolbar.navigationIcon as DrawerArrowDrawable
            val isArrowVisible = animatedDrawable.progress > 0f

            if (isArrowVisible) {
                onCollapseSearchClick()
            } else {
                (activity as NavigationHandler).setDrawerExpanded(true)
            }
        }

        ui.recyclerView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        MPEventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        MPEventBus.getDefault().unregister(this)
    }

    private fun animateToolbarNavigationButton(toArrow: Boolean) {
        val animatedDrawable = ui.toolbar.navigationIcon as DrawerArrowDrawable

        val fromProgress = animatedDrawable.progress
        val toProgress = if (toArrow) 1f else 0f

        ObjectAnimator.ofFloat(animatedDrawable, "progress", fromProgress, toProgress)
            .start()
    }

    override fun showSearchMode(expanded: Boolean) {
        ui.recyclerView.stopScroll()
        ui.search.visibility = if (expanded) View.GONE else View.VISIBLE
        ui.close.visibility = if (expanded) View.VISIBLE else View.GONE
        animateToolbarNavigationButton(expanded)
        (activity as NavigationHandler).setDrawerEnabled(!expanded)
        (activity as NavigationHandler).setBottomNavigationVisible(!expanded)
        if (!expanded) ui.toolbar.hideKeyboard()
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

    override fun showTopRightButton(isClearTextMode: Boolean) {
        with(ui.close) {
            setImageResource(
                if (isClearTextMode) R.drawable.ic_clear_text
                else R.drawable.ic_close
            )
            padding =
                    if (isClearTextMode) dimen(R.dimen.padding_toolbar_icon_big)
                    else dimen(R.dimen.padding_toolbar_icon)
        }
    }

    override fun onExpandSearchClick() {
        animateToolbarNavigationButton(true)
        with(ui) {
            recyclerView.stopScroll()
            setGone(search)
            setVisible(close)
        }
        presenter.startSearchMode()
    }

    override fun onCollapseSearchClick(): Boolean {
        animateToolbarNavigationButton(false)
        val cancelWasVisible = ui.close.visibility == View.VISIBLE
        with(ui) {
            setVisible(search)
            setGone(close)
            search.hideKeyboard()
        }
        presenter.stopSearchMode()
        return cancelWasVisible
    }

    override fun onClearSearchClick() {
        ui.recyclerView.stopScroll()
        presenter.clearSearchQuery()
    }

    override fun onSearch(search: Search) {
        presenter.onSearchQueryChange()
    }

    override fun onStartSearchClick(search: Search) {
        ui.search.hideKeyboard()
        presenter.search()
    }

    override fun onBookClick(book: Book) {
        context!!.startActivity<DetailsActivity>(BooksActivity.ARG_BOOK_ID to book)
    }

    override fun onBuyBookClick(book: UiBook) {
        presenter.onBuyBookClicked(book)
    }

    override fun showBuyDrawableForItem(book: UiBook, @DrawableRes drawableRes: Int) {
        val indexOfBook = adapter.items.indexOf(book)
        adapter.notifyItemChanged(indexOfBook, drawableRes)
    }

    override fun openBookWebsite(book: Book) {
        Analytics.bookAddedToCart(book)
        context!!.openLink(book.website)
    }

    override fun showEmptyProgress(show: Boolean) {
        ui.progressBar.visibility =
                if (show) View.VISIBLE
                else View.GONE
    }

    override fun showEmptyError(show: Boolean, error: Throwable?) {
        longToast(error.toString())
    }

    override fun showEmptyView(show: Boolean) {
        longToast("empty data")
    }

    override fun showData(data: List<UiItem>) {
        adapter.submitList(data)
    }

    override fun showErrorMessage(error: Throwable) {
        longToast(error.toString())
    }

    override fun showRefreshProgress(show: Boolean) {
        TODO("not implemented")
    }

    override fun showPageProgress(visible: Boolean) {
        presenter.setPageProgressVisible(visible, adapter.items)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onDataUpdatedEvent(event: CatalogItemsUpdatedEvent) {
        presenter.loadData()
    }


    companion object {
        private const val ARG_IS_SEARCH_MODE = "ARG_IS_SEARCH_MODE"
        const val ARG_CATEGORY_ID = "ARG_CATEGORY_ID"

        const val UNDEFINED_POSITION = -1
        const val BANNER_POSITION = 0
        const val SEARCH_POSITION = 1
        const val SEARCH_RESULT_POSITION = 3

        fun newInstance(isSearchMode: Boolean = false) =
            CatalogFragment().withArguments(ARG_IS_SEARCH_MODE to isSearchMode)
    }
}

