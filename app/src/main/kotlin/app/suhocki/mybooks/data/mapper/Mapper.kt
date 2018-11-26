package app.suhocki.mybooks.data.mapper

import app.suhocki.mybooks.data.mapper.converter.*
import app.suhocki.mybooks.data.mapper.converter.statistics.*
import javax.inject.Inject

class Mapper @Inject constructor(
    metaDataItemToFile: MetaDataItemToFile,
    statisticsToAuthorStatistics: StatisticsToAuthorStatisticDbo,
    statisticsToPublisherStatistics: StatisticsToPublisherStatisticDbo,
    statisticsToStatusStatistics: StatisticsToStatusStatisticDbo,
    statisticsToYearStatistics: StatisticsToYearStatisticDbo,
    statisticsDataToPriceStatistics: StatisticsToPriceStatisticDbo,
    contactsToShopInfo: ContactsToShopInfoDbo,
    firestoreBookToRoomBook: FirestoreBookToBookDbo,
    roomBannerToFirestoreBanner: BannerDboToFirestoreBanner,
    firestoreCategoryToRoomCategory: FirestoreCategoryToRoomCategory,
    firestoreBannerToRoomBanner: FirestoreBannerToBannerDbo,
    dbBookEntityToUiBookEntity: BookDboToUiBook,
    bannerDboToUiBanner: BannerDboToUiBanner,
    roomCategoryToUiCategory: RoomDboToUiCategory,
    firestoreCategoryToUiCategory: FirestoreCategoryToUiCategory,
    firestoreBannerToUiBanner: FirestoreBannerToUiBanner,
    shopInfoToList: ShopInfoToList
) {

    val converters by lazy {
        mutableListOf<Converter<*, *>>(
            metaDataItemToFile,
            statisticsDataToPriceStatistics,
            statisticsToPublisherStatistics,
            statisticsToYearStatistics,
            statisticsToStatusStatistics,
            statisticsToAuthorStatistics,
            contactsToShopInfo,
            dbBookEntityToUiBookEntity,
            roomBannerToFirestoreBanner,
            firestoreBookToRoomBook,
            firestoreBannerToRoomBanner,
            firestoreCategoryToRoomCategory,
            roomCategoryToUiCategory,
            firestoreCategoryToUiCategory,
            firestoreBannerToUiBanner,
            bannerDboToUiBanner,
            shopInfoToList
        )
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified To> map(input: Any, genericType: Class<out Any>? = null): To {
        if (input is To) return input

        val converter = findConverter<To>(input, genericType)
            ?: throw NoSuchElementException("Cannot find converter from ${input::class.java} to ${To::class.java}")

        return (converter as Converter<Any, To>).convert(input)
    }

    inline fun <reified To> findConverter(
        input: Any,
        genericType: Class<out Any>?
    ): Converter<*, *>? {
        return converters
            .find {
                val isSuitableConverter =
                    it.fromClass.isAssignableFrom(input::class.java) &&
                            To::class.java.isAssignableFrom(it.toClass)

                if (genericType != null)
                    it is GenericConverter && isSuitableConverter &&
                            it.genericType == genericType
                else
                    isSuitableConverter

            }
    }
}