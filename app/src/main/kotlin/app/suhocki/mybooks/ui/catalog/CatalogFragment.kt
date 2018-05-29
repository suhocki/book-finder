package app.suhocki.mybooks.ui.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.di.HeaderCatalogItem
import app.suhocki.mybooks.di.SearchCatalogItem
import app.suhocki.mybooks.domain.model.CatalogItem
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.ui.base.BaseFragment
import app.suhocki.mybooks.ui.books.BooksActivity
import app.suhocki.mybooks.ui.catalog.adapter.CatalogAdapter
import app.suhocki.mybooks.ui.catalog.adapter.CatalogItemType
import app.suhocki.mybooks.ui.catalog.adapter.OnCategoryClickListener
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.ctx
import toothpick.Toothpick
import toothpick.config.Module


class CatalogFragment : BaseFragment(), CatalogView, OnCategoryClickListener {

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
                bind(CatalogItem::class.java)
                    .withName(SearchCatalogItem::class.java)
                    .toInstance(object : CatalogItem {
                        override val type: CatalogItemType
                            get() = CatalogItemType.SEARCH
                    })

                bind(CatalogItem::class.java)
                    .withName(HeaderCatalogItem::class.java)
                    .toInstance(object : CatalogItem {
                        override val type: CatalogItemType
                            get() = CatalogItemType.HEADER
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

    override fun showCatalogItems(catalogItems: List<CatalogItem>) {
        adapter.submitList(catalogItems)
    }

    override fun onCategoryClick(category: Category) {
        context!!.startActivity<BooksActivity>(ARG_CATEGORY to category)
    }

    companion object {
        const val ARG_CATEGORY = "ARG_CATEGORY"

        fun newInstance() = CatalogFragment()
    }
}

