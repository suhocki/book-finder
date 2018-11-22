package app.suhocki.mybooks.di.module

import android.support.v7.widget.RecyclerView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.di.*
import app.suhocki.mybooks.di.provider.CatalogRequestFactoryProvider
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.model.Search
import app.suhocki.mybooks.presentation.base.Paginator
import app.suhocki.mybooks.ui.base.decorator.SearchItemDecoration
import app.suhocki.mybooks.ui.base.decorator.TypeDividerItemDecoration
import com.arellomobile.mvp.viewstate.MvpViewState
import toothpick.config.Module
import java.util.concurrent.atomic.AtomicBoolean

class CatalogModule(
    isSearchMode: Boolean,
    categoriesDividerOffset: Int,
    searchDividerOffset: Int,
    viewState: Any
) : Module() {
    init {
        bind(AtomicBoolean::class.java)
            .withName(IsSearchMode::class.java)
            .toInstance(AtomicBoolean(isSearchMode))

        bind(Search::class.java)
            .withName(SearchAll::class.java)
            .toInstance(SearchEntity(R.string.hint_search))

        bind(RecyclerView.ItemDecoration::class.java)
            .withName(CategoriesDecoration::class.java)
            .toInstance(
                TypeDividerItemDecoration(
                    categoriesDividerOffset,
                    Category::class.java
                )
            )

        bind(RecyclerView.ItemDecoration::class.java)
            .withName(SearchDecoration::class.java)
            .toInstance(SearchItemDecoration(searchDividerOffset))

        bind(Function1::class.java)
            .withName(RequestFactory::class.java)
            .toProvider(CatalogRequestFactoryProvider::class.java)

        bind(Paginator.ViewController::class.java)
            .toInstance(viewState as Paginator.ViewController<*>)

        bind(MvpViewState::class.java)
            .toInstance(viewState as MvpViewState<*>)
    }

    internal class SearchEntity(
        override val hintRes: Int,
        override var searchQuery: String = EMPTY_STRING
    ) : Search

    companion object {
        const val EMPTY_STRING = ""
    }
}