package app.suhocki.mybooks.ui.info.entity

import app.suhocki.mybooks.domain.model.Info

class ContactEntity(
    override val type: Info.InfoType,
    override val name: String,
    override val iconRes: Int,
    override val valueForNavigation: String? = null
) : Info