package app.suhocki.mybooks.ui.search

import android.os.Parcelable
import android.support.annotation.StringRes
import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.di.*
import app.suhocki.mybooks.di.provider.FilterItemStatisticsProvider
import app.suhocki.mybooks.domain.model.Search
import app.suhocki.mybooks.domain.repository.StatisticsRepository
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.security.InvalidKeyException
import javax.inject.Inject

@InjectViewState
class SearchPresenter @Inject constructor(
    @ErrorReceiver private val errorReceiver: (Throwable) -> Unit,
    @SearchKey private val searchKey: String,
    private val statisticsRepository: StatisticsRepository,
    @SearchAuthor private val authorSearchEntity: Search,
    @SearchPublisher private val publisherSearchEntity: Search,
    private val resourceManager: ResourceManager,
    @CategoryId private val categoryId: String
) : MvpPresenter<SearchView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        doAsync(errorReceiver) {
            val titleRes = getTitleRes()
            uiThread { viewState.showTitleRes(titleRes) }
            val searchItems = getSearchItems()
            uiThread {
                viewState.showSearchItems(searchItems)
                viewState.showProgressBar(false)
                if (searchItems.isEmpty()) viewState.showEmptyScreen()
            }
        }
    }

    fun handleClickedItem(parcelable: Parcelable) {
        viewState.finishWithResult(searchKey, parcelable)
    }

    @StringRes
    private fun getTitleRes() = when (searchKey) {
        resourceManager.getString(R.string.hint_search_author) -> R.string.authors

        resourceManager.getString(R.string.hint_search_publisher) -> R.string.publishers

        else -> throw InvalidKeyException()
    }

    private fun getSearchItems(): List<Any> = when (searchKey) {
        resourceManager.getString(R.string.hint_search_author) -> {
            statisticsRepository
                .getAuthorsWithName(authorSearchEntity.searchQuery, categoryId)
                .map { (_, author, count) ->
                    FilterItemStatisticsProvider
                        .FilterAuthorEntity(author, count, isCheckable = false)
                }
        }

        resourceManager.getString(R.string.hint_search_publisher) -> {
            statisticsRepository.getPublishersWithName(publisherSearchEntity.searchQuery, categoryId)
                .map { (_, publisher, count) ->
                    FilterItemStatisticsProvider
                        .FilterPublisherEntity(publisher, count, isCheckable = false)
                }
        }

        else -> throw InvalidKeyException()
    }
}