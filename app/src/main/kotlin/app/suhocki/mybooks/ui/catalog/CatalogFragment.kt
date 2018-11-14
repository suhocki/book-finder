package app.suhocki.mybooks.ui.catalog

import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v7.graphics.drawable.DrawerArrowDrawable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.suhocki.mybooks.*
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.di.module.CatalogModule
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.model.Search
import app.suhocki.mybooks.ui.admin.eventbus.DatabaseUpdatedEvent
import app.suhocki.mybooks.ui.base.BaseFragment
import app.suhocki.mybooks.ui.base.entity.BookEntity
import app.suhocki.mybooks.ui.base.eventbus.CategoriesUpdatedEvent
import app.suhocki.mybooks.ui.base.listener.OnBookClickListener
import app.suhocki.mybooks.ui.base.listener.OnSearchClickListener
import app.suhocki.mybooks.ui.base.listener.OnSearchListener
import app.suhocki.mybooks.ui.base.mpeventbus.MPEventBus
import app.suhocki.mybooks.ui.books.BooksActivity
import app.suhocki.mybooks.ui.catalog.listener.OnCategoryClickListener
import app.suhocki.mybooks.ui.details.DetailsActivity
import app.suhocki.mybooks.ui.main.listener.NavigationHandler
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
import java.util.*
import kotlin.concurrent.schedule


class CatalogFragment : BaseFragment(), CatalogView,
    OnCategoryClickListener,
    OnSearchClickListener,
    OnBookClickListener,
    OnSearchListener {

    private val ui by lazy { CatalogUI<CatalogFragment>() }

    private val adapter by lazy {
        CatalogAdapter(
            this,
            this,
            this,
            this
        )
    }

    @InjectPresenter
    lateinit var presenter: CatalogPresenter

    @ProvidePresenter
    fun providePresenter(): CatalogPresenter {
        val scope = Toothpick.openScopes(DI.APP_SCOPE)
        val catalogModule = CatalogModule(
            arguments!!.getBoolean(ARG_IS_SEARCH_MODE),
            dimen(R.dimen.height_divider_decorator),
            dip(4)
        )
        scope.installModules(catalogModule)

        return scope.getInstance(CatalogPresenter::class.java)
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

    override fun showCatalogItems(
        catalogItems: List<Any>,
        itemDecoration: RecyclerView.ItemDecoration?,
        scrollToPosition: Int,
        updateSearchView: Boolean
    ) = adapter.submitList(catalogItems, onAnimationEnd = {
        with(ui.recyclerView) {
            when (scrollToPosition) {
                SEARCH_POSITION -> {
                    itemDecoration?.let { showRecyclerDecoration(it) }
                    (layoutManager as LinearLayoutManager)
                        .scrollToPositionWithOffset(SEARCH_POSITION, 0)
                    showKeyboard()
                    presenter.removeScrollCommand(catalogItems, itemDecoration)
                }

                BANNER_POSITION -> {
                    Timer().schedule(200) {
                        onUiThread { itemDecoration?.let { showRecyclerDecoration(it) } }
                    }
                    if (updateSearchView) {
                        showBlankSearch()
                        showKeyboard()
                    }
                    scrollToPosition(BANNER_POSITION)
                    presenter.removeScrollCommand(catalogItems, itemDecoration)
                }

                else -> Timer().schedule(200) {
                    onUiThread { itemDecoration?.let { showRecyclerDecoration(it) } }
                }
            }
        }
    })

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

    override fun onCategoryClick(category: Category) {
        context!!.startActivity<BooksActivity>(ARG_CATEGORY to category)
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
        context!!.startActivity<DetailsActivity>(BooksActivity.ARG_BOOK to book)
    }

    override fun onBuyBookClick(book: BookEntity) {
        presenter.onBuyBookClicked(book)
    }

    override fun showBuyDrawableForItem(
        book: BookEntity,
        @DrawableRes drawableRes: Int
    ) {
        val indexOfBook = adapter.items.indexOf(book)
        adapter.notifyItemChanged(indexOfBook, drawableRes)
    }

    override fun openBookWebsite(book: Book) {
        Analytics.bookAddedToCart(book)
        context!!.openLink(book.website)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onDatabaseUpdated(event: DatabaseUpdatedEvent) {
        presenter.loadData()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onCategoriesUpdatedEvent(event: CategoriesUpdatedEvent) {
        presenter.loadData()
    }


    companion object {
        private const val ARG_IS_SEARCH_MODE = "ARG_IS_SEARCH_MODE"
        const val ARG_CATEGORY = "ARG_CATEGORY"

        const val UNDEFINED_POSITION = -1
        const val BANNER_POSITION = 0
        const val SEARCH_POSITION = 1
        const val CATEGORY_POSITION = 2
        const val SEARCH_RESULT_POSITION = 3

        fun newInstance(isSearchMode: Boolean = false) =
            CatalogFragment().withArguments(ARG_IS_SEARCH_MODE to isSearchMode)
    }
}

