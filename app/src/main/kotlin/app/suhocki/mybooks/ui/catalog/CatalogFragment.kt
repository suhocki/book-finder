package app.suhocki.mybooks.ui.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.suhocki.mybooks.R
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.model.Header
import app.suhocki.mybooks.domain.model.Search
import app.suhocki.mybooks.setGone
import app.suhocki.mybooks.setVisible
import app.suhocki.mybooks.ui.base.BaseFragment
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
import toothpick.config.Module


class CatalogFragment : BaseFragment(), CatalogView,
    OnCategoryClickListener, OnSearchClickListener {

    private val ui by lazy { CatalogUI<CatalogFragment>() }

    private val adapter by lazy { CatalogAdapter(this) }

    @InjectPresenter
    lateinit var presenter: CatalogPresenter

    @ProvidePresenter
    fun providePresenter(): CatalogPresenter {
        val scopeName = "CatalogScope_${hashCode()}"
        val scope = Toothpick.openScopes(DI.APP_SCOPE, scopeName)
        scope.installModules(object : Module() {
            init {
                bind(Search::class.java).toInstance(object : Search {
                    override val hintRes: Int
                        get() = R.string.search
                })

                bind(Header::class.java).toInstance(object : Header {
                    override val titleRes: Int
                        get() = R.string.catalog
                })
            }
        })
        return scope.getInstance(CatalogPresenter::class.java).also {
            Toothpick.closeScope(scopeName)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = ui.createView(AnkoContext.create(ctx, this@CatalogFragment))

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ui.recyclerView.adapter = adapter
    }

    override fun showCatalogItems(catalogItems: List<Any>) {
        adapter.submitList(catalogItems)
    }

    override fun showSearchView(expanded: Boolean) {
        ui.search.visibility = if (expanded) View.GONE else View.VISIBLE
        ui.close.visibility = if (expanded) View.VISIBLE else View.GONE
    }

    override fun onCategoryClick(category: Category) {
        context!!.startActivity<BooksActivity>(ARG_CATEGORY to category)
    }

    override fun onSearchClick() {
        with(ui) {
            setGone(menu, search)
            setVisible(back, close)
        }
        (activity as NavigationHandler).setDrawerEnabled(false)
        (activity as NavigationHandler).setBottomNavigationVisible(false)
        presenter.addSearchEntity(adapter.items)

    }

    override fun onCancelSearchClick(): Boolean {
        val cancelWasVisible = ui.close.visibility == View.VISIBLE
        with(ui) {
            setVisible(menu, search)
            setGone(back, close)
        }
        (activity as NavigationHandler).setDrawerEnabled(true)
        (activity as NavigationHandler).setBottomNavigationVisible(true)
        presenter.removeSearchEntity(adapter.items)
        return cancelWasVisible
    }

    companion object {
        const val ARG_CATEGORY = "ARG_CATEGORY"

        fun newInstance() = CatalogFragment()
    }
}

