package app.suhocki.mybooks.di.module

import android.support.v7.widget.RecyclerView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.firestore.FirestoreObserver
import app.suhocki.mybooks.data.firestore.FirestoreRepository
import app.suhocki.mybooks.di.*
import app.suhocki.mybooks.di.provider.CatalogRequestFactoryProvider
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.model.Search
import app.suhocki.mybooks.presentation.base.paginator.PaginationView
import app.suhocki.mybooks.ui.base.decorator.SearchItemDecoration
import app.suhocki.mybooks.ui.base.decorator.TypeDividerItemDecoration
import app.suhocki.mybooks.ui.base.entity.UiItem
import app.suhocki.mybooks.ui.catalog.CatalogView
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
            .toInstance(TypeDividerItemDecoration(categoriesDividerOffset, Category::class.java))

        bind(RecyclerView.ItemDecoration::class.java)
            .withName(SearchDecoration::class.java)
            .toInstance(SearchItemDecoration(searchDividerOffset))

        bind(MvpViewState::class.java)
            .toInstance(viewState as MvpViewState<*>)

        bind(CatalogView::class.java)
            .toInstance(viewState as CatalogView)

        bind(PaginationView::class.java)
            .toInstance(viewState as PaginationView<*>)

        bind(Function1::class.java)
            .withName(CatalogRequestFactory::class.java)
            .toProvider(CatalogRequestFactoryProvider::class.java)

        bind(MutableList::class.java)
            .toInstance(mutableListOf<UiItem>())

        bind(String::class.java)
            .withName(FirestoreCollection::class.java)
            .toInstance(FirestoreRepository.CATEGORIES)

        bind(FirestoreObserver::class.java)
            .singletonInScope()
    }

    internal class SearchEntity(
        override val hintRes: Int,
        override var searchQuery: String = EMPTY_STRING
    ) : Search

    companion object {
        const val EMPTY_STRING = ""
    }
}