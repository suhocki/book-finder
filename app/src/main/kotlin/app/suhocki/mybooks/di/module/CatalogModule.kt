package app.suhocki.mybooks.di.module

import android.content.Context
import android.support.v7.widget.RecyclerView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.di.CategoriesDecoration
import app.suhocki.mybooks.di.SearchAll
import app.suhocki.mybooks.di.SearchDecoration
import app.suhocki.mybooks.domain.model.Header
import app.suhocki.mybooks.domain.model.Search
import app.suhocki.mybooks.ui.base.decorator.DeviderItemDecoration
import app.suhocki.mybooks.ui.base.decorator.SearchItemDecoration
import app.suhocki.mybooks.ui.catalog.CatalogFragment
import org.jetbrains.anko.dimen
import org.jetbrains.anko.dip
import toothpick.config.Module

class CatalogModule(context: Context) : Module() {
    init {
        bind(Search::class.java)
            .withName(SearchAll::class.java)
            .toInstance(SearchEntity(R.string.hint_search))

        bind(Header::class.java).toInstance(
            object : Header {
                override val inverseColors = false
                override var title = context.getString(R.string.catalog)
                override val allCaps = true
            })

        bind(RecyclerView.ItemDecoration::class.java)
            .withName(CategoriesDecoration::class.java)
            .toInstance(
                DeviderItemDecoration(
                    context.dimen(R.dimen.height_catalog_decorator),
                    CatalogFragment.CATEGORY_POSITION
                )
            )

        bind(RecyclerView.ItemDecoration::class.java)
            .withName(SearchDecoration::class.java)
            .toInstance(SearchItemDecoration(context.dip(4)))
    }

    internal class SearchEntity(
        override val hintRes: Int,
        override var searchQuery: String = EMPTY_STRING
    ) : Search

    companion object {
        const val EMPTY_STRING = ""
    }
}