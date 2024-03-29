package app.suhocki.mybooks.di.provider

import android.os.Parcel
import android.os.Parcelable
import app.suhocki.mybooks.di.CategoryId
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.model.filter.*
import app.suhocki.mybooks.domain.model.statistics.FilterItemStatistics
import app.suhocki.mybooks.domain.repository.FilterRepository
import app.suhocki.mybooks.domain.repository.StatisticsRepository
import javax.inject.Inject
import javax.inject.Provider

class FilterItemStatisticsProvider @Inject constructor(
    @CategoryId private val categoryId: String,
    private val statisticsRepository: StatisticsRepository,
    private val filterRepository: FilterRepository
) : Provider<FilterItemStatistics> {

    override fun get(): FilterItemStatistics = object : FilterItemStatistics {

        override var checkedItemCount = 0

        override var checkedSortByCategory = mutableMapOf<Int, Int>()

        override val filterCategories by lazy {
            mutableListOf<FilterCategory>().apply {
                addAll(filterRepository.getFilterCategories())
            }
        }

        override val nameSortItems by lazy {
            mutableListOf<SortName>().apply {
                addAll(filterRepository.getFilterByNameItems())
            }
        }

        override val pricesSortItems by lazy {
            mutableListOf<SortPrice>().apply {
                addAll(filterRepository.getFilterByPriceItems())
            }
        }

        override val authorsFilterItems by lazy {
            mutableListOf<FilterAuthor>().apply {
                addAll(statisticsRepository.getAuthorStatisticsFor(categoryId)
                    .map { (_, author, count) -> FilterAuthorEntity(author, count) })
            }
        }

        override val publishersFilterItems by lazy {
            mutableListOf<FilterPublisher>().apply {
                addAll(statisticsRepository.getPublisherStatisticsFor(categoryId)
                    .map { (_, publisher, count) -> FilterPublisherEntity(publisher, count) })
            }
        }

        override val yearsFilterItems by lazy {
            mutableListOf<FilterYear>().apply {
                addAll(statisticsRepository.getYearStatisticsFor(categoryId)
                    .map { (_, year, count) -> FilterYearEntity(year, count) })
            }
        }

        override val statusesFilterItems by lazy {
            mutableListOf<FilterStatus>().apply {
                addAll(statisticsRepository.getStatusStatisticsFor(categoryId)
                    .map { (_, status, count) -> FilterStatusEntity(status, count) })
            }
        }

        override val authors by lazy {
            mutableMapOf<String, Int>().apply {
                statisticsRepository.getAuthorStatisticsFor(categoryId)
                    .forEach { this[it.author] = it.count }
            }
        }

        override val publishers by lazy {
            mutableMapOf<String, Int>().apply {
                statisticsRepository.getPublisherStatisticsFor(categoryId)
                    .forEach { this[it.publisher] = it.count }
            }
        }

        override val years by lazy {
            mutableMapOf<String, Int>().apply {
                statisticsRepository.getYearStatisticsFor(categoryId)
                    .forEach { this[it.year] = it.count }
            }
        }

        override val statuses by lazy {
            mutableMapOf<String, Int>().apply {
                statisticsRepository.getStatusStatisticsFor(categoryId)
                    .forEach { this[it.status] = it.count }
            }
        }

        override val prices by lazy {
            statisticsRepository.getPriceStatisticsFor(categoryId)
                .let { (_, min, max) -> doubleArrayOf(min, max) }
        }
    }

    internal class FilterYearEntity(
        override val year: String,
        override val booksCount: Int,
        override var isChecked: Boolean = false,
        override var isCheckable: Boolean = true
    ) : FilterYear

    internal class FilterStatusEntity(
        override val status: String,
        override val booksCount: Int,
        override var isChecked: Boolean = false,
        override var isCheckable: Boolean = true
    ) : FilterStatus

    internal class FilterPriceEntity(
        override val hintFrom: Double,
        override val hintTo: Double,
        override var from: Double = 0.0,
        override var to: Double = Int.MAX_VALUE.toDouble()
    ) : FilterPrice

    internal class FilterPublisherEntity(
        override val publisherName: String,
        override val booksCount: Int,
        override var isChecked: Boolean = false,
        override var isCheckable: Boolean = true
    ) : FilterPublisher {
        constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readInt(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte()
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(publisherName)
            parcel.writeInt(booksCount)
            parcel.writeByte(if (isChecked) 1 else 0)
            parcel.writeByte(if (isCheckable) 1 else 0)
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as FilterPublisherEntity

            if (publisherName != other.publisherName) return false

            return true
        }

        override fun hashCode(): Int {
            return publisherName.hashCode()
        }

        companion object CREATOR : Parcelable.Creator<FilterPublisherEntity> {
            override fun createFromParcel(parcel: Parcel): FilterPublisherEntity {
                return FilterPublisherEntity(parcel)
            }

            override fun newArray(size: Int): Array<FilterPublisherEntity?> {
                return arrayOfNulls(size)
            }
        }
    }

    internal class FilterAuthorEntity(
        override val authorName: String,
        override val booksCount: Int,
        override var isChecked: Boolean = false,
        override var isCheckable: Boolean = true
    ) : FilterAuthor {
        constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readInt(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte()
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(authorName)
            parcel.writeInt(booksCount)
            parcel.writeByte(if (isChecked) 1 else 0)
            parcel.writeByte(if (isCheckable) 1 else 0)
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as FilterAuthorEntity

            if (authorName != other.authorName) return false

            return true
        }

        override fun hashCode(): Int {
            return authorName.hashCode()
        }

        companion object CREATOR : Parcelable.Creator<FilterAuthorEntity> {
            override fun createFromParcel(parcel: Parcel): FilterAuthorEntity {
                return FilterAuthorEntity(parcel)
            }

            override fun newArray(size: Int): Array<FilterAuthorEntity?> {
                return arrayOfNulls(size)
            }
        }


    }

}