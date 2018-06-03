package app.suhocki.mybooks.data.repository

import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.domain.model.FilterCategory
import app.suhocki.mybooks.domain.repository.FilterRepository
import javax.inject.Inject


class FilterRepositoryImpl @Inject constructor(
    private val resourceManager: ResourceManager
) : FilterRepository {

    override fun getFilterCategories(): List<FilterCategory> =
        resourceManager.getStringArray(R.array.filter_categories)
            .map {
                object : FilterCategory {
                    override val title = it

                    override var isExpanded = false

                    override var isConfigurated = false
                }
            }
}