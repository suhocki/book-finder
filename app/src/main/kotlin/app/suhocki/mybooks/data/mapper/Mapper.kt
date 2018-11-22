package app.suhocki.mybooks.data.mapper

import app.suhocki.mybooks.data.mapper.converter.*
import app.suhocki.mybooks.data.mapper.converter.statistics.*
import javax.inject.Inject

class Mapper @Inject constructor(
    metaDataItemToFile: MetaDataItemToFile,
    statisticsToAuthorStatistics: StatisticsToAuthorStatistics,
    statisticsToPublisherStatistics: StatisticsToPublisherStatistics,
    statisticsToStatusStatistics: StatisticsToStatusStatistics,
    statisticsToYearStatistics: StatisticsToYearStatistics,
    statisticsDataToPriceStatistics: StatisticsToPriceStatistics,
    contactsToShopInfo: ContactsToShopInfo,
    firestoreBookToRoomBook: FirestoreBookToRoomBook,
    roomBannerToFirestoreBanner: RoomBannerToFirestoreBanner,
    firestoreCategoryToRoomCategory: FirestoreCategoryToRoomCategory,
    firestoreBannerToRoomBanner: FirestoreBannerToRoomBanner,
    dbBookEntityToUiBookEntity: DbBookEntityToUiBookEntity,
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
            shopInfoToList
        )
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified To> map(input: Any, genericType: Class<out Any>? = null): To {
        if (input is To) return input

        val converter = converters
            .find {
                val isSuitableConverter =
                    it.fromClass.isAssignableFrom(input::class.java) &&
                            To::class.java.isAssignableFrom(it.toClass)

                if (genericType != null)
                    it is GenericConverter && isSuitableConverter &&
                            it.genericType == genericType
                else
                    isSuitableConverter

            } as Converter<Any, To>

        return converter.convert(input)
    }
}