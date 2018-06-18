package app.suhocki.mybooks.di.provider.ads

import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.domain.model.BannerAd
import javax.inject.Inject
import javax.inject.Provider

class BannerAdProvider @Inject constructor(
    private val resourceManager: ResourceManager
) : Provider<BannerAd> {

    override fun get() = object : BannerAd {
        override val bannerId = resourceManager.getString(R.string.banner_ad_id)
    }
}
