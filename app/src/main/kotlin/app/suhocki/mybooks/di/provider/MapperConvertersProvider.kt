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
    private val bannerDboToFirestoreBanner: BannerDboToFirestoreBanner,
    private val firestoreCategoryToRoomCategory: FirestoreCategoryToCategoryDbo,
    private val firestoreBannerToRoomBanner: FirestoreBannerToBannerDbo,
    private val bookDboToUiBookEntity: BookDboToUiBook,
    private val bannerDboToUiBanner: BannerDboToUiBanner,
    private val categoryDboToUiCategory: RoomDboToUiCategory,
    private val firestoreCategoryToUiCategory: FirestoreCategoryToUiCategory,
    private val firestoreBannerToUiBanner: FirestoreBannerToUiBanner,
    private val categoryDboToFirestoreCategory: CategoryDboToFirestoreCategory,
    private val shopInfoToList: ShopInfoToList,
    private val firestoreSnapshotToUiCategories: FirestoreDocumentToUiCategory,
    private val bookDboToFirestoreBook: BookDboToFirestoreBook
) : Provider<Set<Converter<*, *>>> {

    override fun get() = setOf(
        metaDataItemToFile,
        statisticsToAuthorStatistics,
        statisticsToPublisherStatistics,
        statisticsToStatusStatistics,
        statisticsToYearStatistics,
        statisticsDataToPriceStatistics,
        contactsToShopInfo,
        bannerDboToFirestoreBanner,
        bookDboToUiBookEntity,
        bookDboToFirestoreBook,
        bannerDboToUiBanner,
        categoryDboToFirestoreCategory,
        categoryDboToUiCategory,
        firestoreBookToRoomBook,
        firestoreCategoryToRoomCategory,
        firestoreBannerToRoomBanner,
        firestoreSnapshotToUiCategories,
        firestoreCategoryToUiCategory,
        firestoreBannerToUiBanner,
        shopInfoToList
    )
}