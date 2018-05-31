package app.suhocki.mybooks.ui.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.model.Search
import app.suhocki.mybooks.hideKeyboard
import app.suhocki.mybooks.setGone
import app.suhocki.mybooks.setVisible
import app.suhocki.mybooks.showKeyboard
import app.suhocki.mybooks.ui.base.BaseFragment
import app.suhocki.mybooks.ui.base.ScrollLayoutManager
import app.suhocki.mybooks.ui.base.listener.NavigationHandler
import app.suhocki.mybooks.ui.base.listener.OnCategoryClickListener
import app.suhocki.mybooks.ui.base.listener.OnSearchClickListener
import app.suhocki.mybooks.ui.books.BooksActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.ctx
import toothpick.Toothpick
import javax.inject.Inject


class CatalogFragment : BaseFragment(), CatalogView,
    OnCategoryClickListener, OnSearchClickListener {

    private val ui by lazy { CatalogUI<CatalogFragment>() }

    @Inject
    lateinit var search: Search

    private val adapter by lazy { CatalogAdapter(this, search) }

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
    ) = adapter.submitList(catalogItems, onAnimationEndAction = {
        if (scrollToPosition != UNDEFINED_POSITION) with(ui.recyclerView) {
            stopScroll()
            when (scrollToPosition) {
                SEARCH_POSITION -> {
                    (layoutManager as ScrollLayoutManager)
                        .scrollToPositionWithOffset(SEARCH_POSITION, 0)
                    showKeyboard()
                }

                BANNER_POSITION -> {
                    scrollToPosition(BANNER_POSITION)
                }
            }
            presenter.clearScrollToPositionCommand(catalogItems)
        }
    })


    override fun showSearchMode(expanded: Boolean) {
        ui.search.visibility = if (expanded) View.GONE else View.VISIBLE
        ui.close.visibility = if (expanded) View.VISIBLE else View.GONE
        (activity as NavigationHandler).setDrawerEnabled(!expanded)
        (activity as NavigationHandler).setBottomNavigationVisible(!expanded)
    }

    override fun onCategoryClick(category: Category) {
        context!!.startActivity<BooksActivity>(ARG_CATEGORY to category)
    }

    override fun onSearchClick() {
        with(ui) {
            setGone(menu, search)
            setVisible(back, close)
        }
        presenter.startSearchMode()
    }

    override fun onCancelSearchClick(): Boolean {
        val cancelWasVisible = ui.close.visibility == View.VISIBLE
        with(ui) {
            setVisible(menu, search)
            setGone(back, close)
            search.hideKeyboard()
        }
        presenter.stopSearchMode()
        return cancelWasVisible
    }

    companion object {
        const val ARG_CATEGORY = "ARG_CATEGORY"
        const val UNDEFINED_POSITION = -1
        const val BANNER_POSITION = 0
        const val SEARCH_POSITION = 1

        fun newInstance() = CatalogFragment()
    }
}

