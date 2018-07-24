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
            .apply {
                this[0].groupItem = this[1]
                this[1].groupItem = this[0]
            }

    override fun getFilterByPriceItems(): List<SortPrice> =
        resourceManager.getStringArray(R.array.sort_by_name)
            .map { FilterPriceEntity(it) }
            .apply {
                this[0].groupItem = this[1]
                this[1].groupItem = this[0]
            }

    private inner class FilterCategoryEntity(
        override var title: String,
        override var isExpanded: Boolean = false,
        override var checkedCount: Int = 0,
        override val inverseColors: Boolean = false,
        override val allCaps: Boolean = false
    ) : FilterCategory

    private inner class FilterNameEntity(
        override val sortName: String,
        override var isCheckable: Boolean = true,
        override var isChecked: Boolean = false,
        override var groupItem: SortName? = null
    ) : SortName

    private inner class FilterPriceEntity(
        override val sortName: String,
        override var isChecked: Boolean = false,
        override var isCheckable: Boolean = true,
        override var groupItem: SortPrice? = null
    ) : SortPrice
}