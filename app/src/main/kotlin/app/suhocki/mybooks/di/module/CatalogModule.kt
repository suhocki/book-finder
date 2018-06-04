package app.suhocki.mybooks.di.module

import android.content.Context
import android.support.v7.widget.RecyclerView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.di.CategoriesDecoration
import app.suhocki.mybooks.di.SearchDecoration
import app.suhocki.mybooks.domain.model.Header
import app.suhocki.mybooks.domain.model.Hint
import app.suhocki.mybooks.domain.model.Search
import org.jetbrains.anko.dimen
import org.jetbrains.anko.dip
import toothpick.config.Module

class CatalogModule(context: Context) : Module() {
    init {
        bind(Search::class.java).toInstance(object : Search {
            override var searchQuery: String = EMPTY_STRING

            override val hintRes = R.string.hint_search
        })

        bind(Header::class.java).toInstance(object : Header {
            override var titleRes = R.string.catalog
        })

        bind(Hint::class.java).toInstance(object : Hint {
            override val hintRes = R.string.hint_search
        })

        bind(RecyclerView.ItemDecoration::class.java)
            .withName(CategoriesDecoration::class.java)
            .toInstance(
                app.suhocki.mybooks.ui.base.decorator.CategoriesItemDecoration(
                    context.dimen(
                        R.dimen.height_catalog_decorator
                    )
                )
            )

        bind(RecyclerView.ItemDecoration::class.java)
            .withName(SearchDecoration::class.java)
            .toInstance(app.suhocki.mybooks.ui.base.decorator.SearchItemDecoration(context.dip(4)))
    }

    companion object {
        const val EMPTY_STRING = ""
    }
}