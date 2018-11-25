package app.suhocki.mybooks.ui.catalog.entity

import app.suhocki.mybooks.domain.model.BannerAd
import app.suhocki.mybooks.ui.base.entity.UiItem

class UiBannerAd(
    override val bannerId: String,
    override var isNextPageTrigger: Boolean = false
) : BannerAd, UiItem