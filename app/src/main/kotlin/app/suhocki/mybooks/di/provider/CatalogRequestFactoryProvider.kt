package app.suhocki.mybooks.di.provider

import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.ads.AdsManager
import app.suhocki.mybooks.data.remoteconfig.RemoteConfiguration
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.di.Firestore
import app.suhocki.mybooks.di.Room
import app.suhocki.mybooks.domain.model.Banner
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.repository.BannersRepository
import app.suhocki.mybooks.domain.repository.CategoriesRepository
import app.suhocki.mybooks.ui.catalog.entity.HeaderEntity
import javax.inject.Inject
import javax.inject.Provider

class CatalogRequestFactoryProvider @Inject constructor(
    @Room private val localBannersRepository: BannersRepository,
    @Room private val localCategoriesRepository: CategoriesRepository,

    @Firestore private val remoteBannersRepository: BannersRepository,
    @Firestore private val remoteCategoriesRepository: CategoriesRepository,

    private val remoteConfigurator: RemoteConfiguration,
    private val adsManager: AdsManager,
    private val resourceManager: ResourceManager
) : Provider<(Int) -> List<Any>> {

    override fun get(): (Int) -> List<Any> = { page ->
        val data = mutableListOf<Any>()
        val banner: Any? =
            if (remoteConfigurator.isBannerAdEnabled) adsManager.getBannerAd()
            else getBanners().firstOrNull()
        val categories = getCategories()

        if (banner != null) {
            data.add(banner)
            data.add(HeaderEntity(resourceManager.getString(R.string.catalog)))
        }
        data.addAll(categories)

        data
    }

    private fun getCategories(): List<Category> {
        val localCategories = localCategoriesRepository.getCategories()

        return if (localCategories.isNotEmpty()) {
            localCategories
        } else {
            val remoteCategories = remoteCategoriesRepository.getCategories()
            localCategoriesRepository.addCategories(remoteCategories)
            remoteCategories
        }
    }

    private fun getBanners(): List<Banner> {
        val localBanners = localBannersRepository.getBanners()

        return if (localBanners.isNotEmpty()) {
            localBanners
        } else {
            val remoteBanners = remoteBannersRepository.getBanners()
            localBannersRepository.setBanners(remoteBanners)
            remoteBanners
        }
    }
}