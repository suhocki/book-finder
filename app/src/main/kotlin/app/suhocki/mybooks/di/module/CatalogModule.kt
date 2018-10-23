package app.suhocki.mybooks.di.module

import android.support.v7.widget.RecyclerView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.di.*
import app.suhocki.mybooks.domain.model.Search
import app.suhocki.mybooks.ui.base.decorator.DividerItemDecoration
import app.suhocki.mybooks.ui.base.decorator.SearchItemDecoration
import app.suhocki.mybooks.ui.catalog.CatalogFragment
import toothpick.config.Module

class CatalogModule(
    isSearchMode: Boolean,
    categoriesDividerOffset: Int,
    searchDividerOffset: Int
) : Module() {
    init {
        bind(PrimitiveWrapper::class.java)
            .withName(IsSearchMode::class.java)
            .toInstance(PrimitiveWrapper(isSearchMode))

        bind(Search::class.java)
            .withName(SearchAll::class.java)
            .toInstance(SearchEntity(R.string.hint_search))

        bind(RecyclerView.ItemDecoration::class.java)
            .withName(CategoriesDecoration::class.java)
            .toInstance(
                DividerItemDecoration(
                    categoriesDividerOffset,
                    CatalogFragment.CATEGORY_POSITION
                )
            )

        bind(RecyclerView.ItemDecoration::class.java)
            .withName(SearchDecoration::class.java)
            .toInstance(SearchItemDecoration(searchDividerOffset))
    }

    internal class SearchEntity(
        override val hintRes: Int,
        override var searchQuery: String = EMPTY_STRING
    ) : Search

    companion object {
        const val EMPTY_STRING = ""
    }
}