package app.suhocki.mybooks.di.module

import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.filter.FilterRepositoryImpl
import app.suhocki.mybooks.di.SearchAuthor
import app.suhocki.mybooks.di.SearchPublisher
import app.suhocki.mybooks.di.provider.FilterItemStatisticsProvider
import app.suhocki.mybooks.di.provider.FilterPriceEntityProvider
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.model.Search
import app.suhocki.mybooks.domain.model.filter.FilterPrice
import app.suhocki.mybooks.domain.model.statistics.FilterItemStatistics
import app.suhocki.mybooks.domain.repository.FilterRepository
import toothpick.config.Module

class BooksModule(category: Category) : Module() {

    init {
        bind(FilterRepository::class.java)
            .to(FilterRepositoryImpl::class.java)
            .singletonInScope()

        bind(Category::class.java)
            .toInstance(category)

        bind(FilterItemStatistics::class.java)
            .toProvider(FilterItemStatisticsProvider::class.java)
            .providesSingletonInScope()

        bind(Search::class.java)
            .withName(SearchAuthor::class.java)
            .toInstance(SearchEntity(R.string.hint_search_author))

        bind(Search::class.java)
            .withName(SearchPublisher::class.java)
            .toInstance(SearchEntity(R.string.hint_search_publisher))

        bind(FilterPrice::class.java)
            .toProvider(FilterPriceEntityProvider::class.java)
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