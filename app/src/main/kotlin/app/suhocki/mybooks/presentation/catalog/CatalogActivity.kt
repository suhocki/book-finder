package app.suhocki.mybooks.presentation.catalog

import android.os.Bundle
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.di.module.CatalogActivityModule
import app.suhocki.mybooks.domain.model.CatalogItem
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.presentation.books.BooksActivity
import app.suhocki.mybooks.presentation.catalog.adapter.CatalogAdapter
import app.suhocki.mybooks.presentation.catalog.adapter.OnCategoryClickListener
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.startActivity
import toothpick.Toothpick


class CatalogActivity : MvpAppCompatActivity(), CatalogView, OnCategoryClickListener {

    @InjectPresenter
    lateinit var presenter: CatalogPresenter

    private val layout = CatalogUI()

    private val adapter = CatalogAdapter()

    @ProvidePresenter
    fun providePresenter(): CatalogPresenter =
        Toothpick.openScope(DI.APP_SCOPE)
            .apply {installModules(CatalogActivityModule())}
            .getInstance(CatalogPresenter::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val scope = Toothpick.openScopes(DI.APP_SCOPE, DI.CATALOG_ACTIVITY_SCOPE)
        Toothpick.inject(this@CatalogActivity, scope)
        layout.apply {
            setContentView(this@CatalogActivity)
            recyclerView.adapter = adapter
        }
        adapter.setOnCategoryClickListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) Toothpick.closeScope(DI.CATALOG_ACTIVITY_SCOPE)
    }

    override fun showCatalogItems(catalogItems: List<CatalogItem>) {
        adapter.submitList(catalogItems)
    }

    override fun onCategoryClick(category: Category) {
        startActivity<BooksActivity>(ARG_CATEGORY to category)
    }

    companion object {
        const val ARG_CATEGORY = "ARG_CATEGORY"
    }
}

