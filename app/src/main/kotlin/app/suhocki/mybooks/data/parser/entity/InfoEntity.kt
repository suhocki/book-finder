package app.suhocki.mybooks.data.parser.entity

import app.suhocki.mybooks.domain.model.Info

class InfoEntity(
    override val type: Info.InfoType,
    override val name: String,
    override val valueForNavigation: String? = null,
    override val iconRes: Int = 0
) : Info