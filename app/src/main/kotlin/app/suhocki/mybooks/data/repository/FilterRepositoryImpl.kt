package app.suhocki.mybooks.data.repository

import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.domain.model.filter.FilterCategory
import app.suhocki.mybooks.domain.model.filter.SortName
import app.suhocki.mybooks.domain.model.filter.SortPrice
import app.suhocki.mybooks.domain.repository.FilterRepository
import javax.inject.Inject


class FilterRepositoryImpl @Inject constructor(
    private val resourceManager: ResourceManager
) : FilterRepository {

    override fun getFilterCategories(): List<FilterCategory> =
        resourceManager.getStringArray(R.array.filter_categories)
            .map { FilterCategoryEntity(it) }

    override fun getFilterByNameItems(): List<SortName> =
        resourceManager.getStringArray(R.array.sort_by_name)
            .map { FilterNameEntity(it) }

    override fun getFilterByPriceItems(): List<SortPrice> =
        resourceManager.getStringArray(R.array.sort_by_name)
            .map { FilterPriceEntity(it) }

    private inner class FilterCategoryEntity(
        override val title: String,
        override var isExpanded: Boolean = false,
        override var isConfigurated: Boolean = false
    ) : FilterCategory

    private inner class FilterNameEntity(
        override val sortName: String,
        override var isChecked: Boolean = false
    ) : SortName

    private inner class FilterPriceEntity(
        override val sortName: String,
        override var isChecked: Boolean = false
    ) : SortPrice
}