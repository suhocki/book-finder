package app.suhocki.mybooks.ui.filter.entity

import app.suhocki.mybooks.domain.model.filter.FilterCategory

internal class FilterCategoryEntity(
    override val title: String,
    override var isExpanded: Boolean,
    override var checkedCount: Int
) : FilterCategory