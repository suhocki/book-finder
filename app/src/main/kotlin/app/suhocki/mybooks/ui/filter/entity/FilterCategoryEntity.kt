package app.suhocki.mybooks.ui.filter.entity

import app.suhocki.mybooks.domain.model.filter.FilterCategory

internal class FilterCategoryEntity(
    override var title: String,
    override var isExpanded: Boolean,
    override var checkedCount: Int,
    override val inverseColors: Boolean = false,
    override val allCaps: Boolean = false
) : FilterCategory