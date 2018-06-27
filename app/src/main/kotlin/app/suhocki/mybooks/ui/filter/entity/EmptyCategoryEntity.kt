package app.suhocki.mybooks.ui.filter.entity

import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.domain.model.filter.EmptyCategory
import app.suhocki.mybooks.domain.model.filter.FilterCategory
import java.security.InvalidKeyException

internal class EmptyCategoryEntity(
    override val name: String,
    override val categoryTitle: String
) : EmptyCategory {

    constructor(category: FilterCategory, resourceManager: ResourceManager) : this(
        when (category.title) {
            resourceManager.getString(R.string.author) ->
                resourceManager.getString(R.string.author_not_found)

            resourceManager.getString(R.string.publisher) ->
                resourceManager.getString(R.string.publisher_not_found)

            resourceManager.getString(R.string.year) ->
                resourceManager.getString(R.string.year_not_found)

            resourceManager.getString(R.string.status) ->
                resourceManager.getString(R.string.status_not_found)

            else -> throw InvalidKeyException()
        },
        category.title
    )

}