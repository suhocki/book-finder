package app.suhocki.mybooks.ui.filter

import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.error.ErrorHandler
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.domain.FilterInteractor
import app.suhocki.mybooks.domain.model.filter.*
import app.suhocki.mybooks.domain.model.statistics.FilterItemStatistics
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.security.InvalidKeyException
import javax.inject.Inject

@InjectViewState
class FilterPresenter @Inject constructor(
    private val filterInteractor: FilterInteractor,
    private val filterItemStatistics: FilterItemStatistics,
    private val resourceManager: ResourceManager,
    private val errorHandler: ErrorHandler
) : MvpPresenter<FilterView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        doAsync(errorHandler.errorReceiver) {
            val filterItems = filterInteractor.getFilterCategories().toList()
            uiThread {
                viewState.showFilterItems(filterItems)
            }
        }
    }

    fun collapseFilterCategory(
        filterCategory: FilterCategory,
        items: MutableList<Any>
    ) = doAsync(errorHandler.errorReceiver) {
        val filterCategoryIndex = items.indexOf(filterCategory)
        val filterItems = mutableListOf<Any>().apply {
            addAll(items)
            set(filterCategoryIndex, object : FilterCategory {
                override val title = filterCategory.title
                override var isExpanded = false
                override var isConfigurated = filterCategory.isConfigurated
            })
            removeAll {
                when (filterCategory.title) {
                    resourceManager.getString(R.string.author) ->
                        it is FilterAuthor

                    resourceManager.getString(R.string.publisher) ->
                        it is FilterPublisher

                    resourceManager.getString(R.string.year) ->
                        it is FilterYear

                    resourceManager.getString(R.string.availability) ->
                        it is FilterStatus

                    else -> throw InvalidKeyException()
                }
            }
        }
        uiThread {
            viewState.showFilterItems(filterItems)
        }
    }

    fun expandFilterCategory(
        filterCategory: FilterCategory,
        items: MutableList<Any>
    ) = doAsync(errorHandler.errorReceiver) {
        val filterCategoryIndex = items.indexOf(filterCategory)
        val filterItems = mutableListOf<Any>().apply {
            addAll(items)
            set(filterCategoryIndex, object : FilterCategory {
                override val title = filterCategory.title
                override var isExpanded = true
                override var isConfigurated = filterCategory.isConfigurated
            })
        }
        filterItems.addAll(
            filterCategoryIndex + 1,
            when (filterCategory.title) {
                resourceManager.getString(R.string.author) ->
                    filterItemStatistics.authorsFilterItems

                resourceManager.getString(R.string.publisher) ->
                    filterItemStatistics.publishersFilterItems

                resourceManager.getString(R.string.year) ->
                    filterItemStatistics.yearsFilterItems

                resourceManager.getString(R.string.availability) ->
                    filterItemStatistics.statusesFilterItems

                else -> throw InvalidKeyException()
            }
        )
        uiThread {
            viewState.showFilterItems(filterItems)
        }
    }
}