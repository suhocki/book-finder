package app.suhocki.mybooks.di.module

import app.suhocki.mybooks.data.repository.FilterRepositoryImpl
import app.suhocki.mybooks.di.provider.FilterItemStatisticsProvider
import app.suhocki.mybooks.domain.model.Category
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
    }
}