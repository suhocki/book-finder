package app.suhocki.mybooks.di.provider

import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.ads.AdsManager
import app.suhocki.mybooks.data.firestore.FirestoreObserver
import app.suhocki.mybooks.data.mapper.Mapper
import app.suhocki.mybooks.data.remoteconfig.RemoteConfiguration
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.di.Firestore
import app.suhocki.mybooks.di.Room
import app.suhocki.mybooks.domain.model.Banner
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.repository.BannersRepository
import app.suhocki.mybooks.domain.repository.CategoriesRepository
import app.suhocki.mybooks.ui.base.entity.UiItem
import app.suhocki.mybooks.ui.catalog.entity.UiBanner
import app.suhocki.mybooks.ui.catalog.entity.UiCategory
import app.suhocki.mybooks.ui.catalog.entity.UiHeader
import org.jetbrains.anko.doAsync
import javax.inject.Inject
import javax.inject.Provider

class CatalogRequestFactoryProvider @Inject constructor(
    @Room private val localBannersRepository: BannersRepository,
    @Room private val localCategoriesRepository: CategoriesRepository,

    @Firestore private val remoteBannersRepository: BannersRepository,

    private val firestoreObserver: FirestoreObserver,
    private val mapper: Mapper,
    private val remoteConfigurator: RemoteConfiguration,
    private val adsManager: AdsManager,
    private val resourceManager: ResourceManager
) : Provider<(Int) -> List<UiItem>> {

    override fun get(): (Int) -> List<UiItem> = { page ->
        val data = mutableListOf<UiItem>()
        val categories = getCategories(page.dec() * ITEMS_PER_PAGE, ITEMS_PER_PAGE)
            .map { mapper.map<UiCategory>(it) }
            .apply { setNextPageTrigger(this) }

//        if (page == Paginator.FIRST_PAGE) fillWithBannerAndHeader(data)
        data.apply { addAll(categories) }
    }

    private fun fillWithBannerAndHeader(data: MutableList<UiItem>) {
        val banner: UiItem? =
            if (remoteConfigurator.isBannerAdEnabled) adsManager.getBannerAd()
            else getBanners().asSequence().map { mapper.map<UiBanner>(it) }.firstOrNull()
        if (banner != null) {
            data.add(banner)
            data.add(UiHeader(resourceManager.getString(R.string.catalog)))
        }
    }

    private fun setNextPageTrigger(list: List<UiItem>) = with(list) {
        val nextPageTriggerPosition =
            if (size > TRIGGER_OFFSET) size - TRIGGER_OFFSET
            else lastIndex

        list.getOrNull(nextPageTriggerPosition)?.let { it.isNextPageTrigger = true }
    }

    private fun getCategories(offset: Int, limit: Int): List<Category> =
        localCategoriesRepository.getCategories(offset, limit).let {
            if (it.isNotEmpty()) {
                doAsync { firestoreObserver.observeCategories(offset, limit) }
                it
            } else firestoreObserver.observeCategories(offset, limit)
        }

    private fun getBanners(): List<Banner> {
        val localBanners = localBannersRepository.getBanners()

        return if (localBanners.isNotEmpty()) {
            localBanners
        } else {
            val remoteBanners = remoteBannersRepository.getBanners()
//            localBannersRepository.setBanners(remoteBanners)
            remoteBanners
        }
    }


    companion object {
        const val ITEMS_PER_PAGE = 15
        const val TRIGGER_OFFSET = 1
    }
}