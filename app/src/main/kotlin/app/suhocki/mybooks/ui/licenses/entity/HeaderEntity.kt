package app.suhocki.mybooks.ui.licenses.entity

import app.suhocki.mybooks.domain.model.Header

data class HeaderEntity(
    override var title: String,
    override val inverseColors: Boolean = true,
    override val allCaps: Boolean = false
) : Header