package app.suhocki.mybooks.ui.info.entity

import app.suhocki.mybooks.domain.model.Header

class HeaderEntity(
    override var title: String,
    override val inverseColors: Boolean = false
) : Header