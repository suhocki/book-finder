package app.suhocki.mybooks.domain

import android.support.annotation.StringRes
import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.di.SearchAuthor
import app.suhocki.mybooks.di.SearchKey
import app.suhocki.mybooks.di.SearchPublisher
import app.suhocki.mybooks.di.provider.FilterItemStatisticsProvider
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.model.Search
import app.suhocki.mybooks.domain.repository.StatisticsRepository
import java.security.InvalidKeyException
import javax.inject.Inject

class SearchInteractor @Inject constructor(
    private val statisticsRepository: StatisticsRepository,
    @SearchAuthor private val authorSearchEntity: Search,
    @SearchPublisher private val publisherSearchEntity: Search,
    @SearchKey private val searchKey: String,
    private val resourceManager: ResourceManager,
    private val category: Category
) {

    @StringRes
    fun getTitleRes() = when (searchKey) {
        resourceManager.getString(R.string.hint_search_author) -> R.string.authors

        resourceManager.getString(R.string.hint_search_publisher) -> R.string.publishers

        else -> throw InvalidKeyException()
    }

    fun getSearchItems(): List<Any> = when (searchKey) {
        resourceManager.getString(R.string.hint_search_author) -> {
            statisticsRepository.getAuthorsWithName(
                authorSearchEntity.searchQuery,
                category
            ).map { (_, author, count) ->
                FilterItemStatisticsProvider.FilterAuthorEntity(author, count, isCheckable = false)
            }
        }

        resourceManager.getString(R.string.hint_search_publisher) -> {
            statisticsRepository.getPublishersWithName(
                publisherSearchEntity.searchQuery,
                category
            ).map { (_, publisher, count) ->
                FilterItemStatisticsProvider.FilterPublisherEntity(publisher, count, isCheckable = false)
            }
        }

        else -> throw InvalidKeyException()
    }
}