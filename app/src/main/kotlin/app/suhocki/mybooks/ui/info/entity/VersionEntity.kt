package app.suhocki.mybooks.ui.info.entity

import app.suhocki.mybooks.domain.model.Version

class VersionEntity(
    override val version: String,
    override val code: Int
) : Version