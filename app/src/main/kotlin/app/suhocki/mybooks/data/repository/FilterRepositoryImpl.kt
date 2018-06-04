package app.suhocki.mybooks.data.repository

import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.domain.model.filter.FilterCategory
import app.suhocki.mybooks.domain.repository.FilterRepository
import javax.inject.Inject


class FilterRepositoryImpl @Inject constructor(
    private val resourceManager: ResourceManager
) : FilterRepository {

    private inner class FilterCategoryEntity(
        override val title: String,
        override var isExpanded: Boolean,
        override var isConfigurated: Boolean
    ) : FilterCategory

    override fun getFilterCategories(): List<FilterCategory> =
        resourceManager.getStringArray(R.array.filter_categories)
            .map { FilterCategoryEntity(it, false, false) }
}