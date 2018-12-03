package app.suhocki.mybooks.di.provider

import app.suhocki.mybooks.data.mapper.Converter
import app.suhocki.mybooks.data.mapper.converter.*
import app.suhocki.mybooks.data.mapper.converter.statistics.*
import javax.inject.Inject
import javax.inject.Provider

class MapperConvertersProvider @Inject constructor(
    private val metaDataItemToFile: MetaDataItemToFile,
    private val statisticsToAuthorStatistics: StatisticsToAuthorStatisticDbo,
    private val statisticsToPublisherStatistics: StatisticsToPublisherStatisticDbo,
    private val statisticsToStatusStatistics: StatisticsToStatusStatisticDbo,
    private val statisticsToYearStatistics: StatisticsToYearStatisticDbo,
    private val statisticsDataToPriceStatistics: StatisticsToPriceStatisticDbo,
    private val contactsToShopInfo: ContactsToShopInfoDbo,
    private val firestoreBookToRoomBook: FirestoreBookToBookDbo,
    private val roomBannerToFirestoreBanner: BannerDboToFirestoreBanner,
    private val firestoreCategoryToRoomCategory: FirestoreCategoryToRoomCategory,
    private val firestoreBannerToRoomBanner: FirestoreBannerToBannerDbo,
    private val dbBookEntityToUiBookEntity: BookDboToUiBook,
    private val bannerDboToUiBanner: BannerDboToUiBanner,
    private val roomCategoryToUiCategory: RoomDboToUiCategory,
    private val firestoreCategoryToUiCategory: FirestoreCategoryToUiCategory,
    private val firestoreBannerToUiBanner: FirestoreBannerToUiBanner,
    private val shopInfoToList: ShopInfoToList
) : Provider<Set<Converter<*, *>>> {

    override fun get() =
        setOf(
            metaDataItemToFile,
            statisticsToAuthorStatistics,
            statisticsToPublisherStatistics,
            statisticsToStatusStatistics,
            statisticsToYearStatistics,
            statisticsDataToPriceStatistics,
            contactsToShopInfo,
            firestoreBookToRoomBook,
            roomBannerToFirestoreBanner,
            firestoreCategoryToRoomCategory,
            firestoreBannerToRoomBanner,
            dbBookEntityToUiBookEntity,
            bannerDboToUiBanner,
            roomCategoryToUiCategory,
            firestoreCategoryToUiCategory,
            firestoreBannerToUiBanner,
            shopInfoToList
        )
}