package app.suhocki.mybooks.domain

import app.suhocki.mybooks.data.database.BooksDatabase
import app.suhocki.mybooks.data.database.entity.*
import app.suhocki.mybooks.data.googledrive.RemoteFilesRepository
import app.suhocki.mybooks.data.localstorage.LocalFilesRepository
import app.suhocki.mybooks.data.parser.entity.StatisticsEntity
import app.suhocki.mybooks.domain.model.Banner
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.model.Info
import app.suhocki.mybooks.domain.repository.*
import java.io.File
import javax.inject.Inject

class BackgroundInteractor @Inject constructor(
    private val remoteFilesRepository: RemoteFilesRepository,
    private val localFilesRepository: LocalFilesRepository,
    private val bookDatabaseRepository: BooksRepository,
    private val bannersRepository: BannersRepository,
    private val statisticDatabaseRepository: StatisticsRepository,
    private val settingsRepository: SettingsRepository,
    private val infoRepository: InfoRepository
) {
    fun getDownloadedFile(fileId: String) =
            localFilesRepository.getDownloadedFile(fileId)

    fun downloadFile(fileId: String) =
        remoteFilesRepository.downloadFile(fileId)

    fun saveFile(folder: String, fileName: String, bytes: ByteArray) =
        localFilesRepository.saveFile(folder, fileName, bytes)

    fun unzip(file: File) =
        localFilesRepository.unzip(file)

    fun parseXlsStructure(xlsFile: File) =
        localFilesRepository.parseXlsStructure(xlsFile)

    fun extractXlsDocument(strings: ArrayList<String>) =
        localFilesRepository.extractXlsDocument(strings)

    fun saveBooksData(data: Map<out Category, Collection<Book>>) {
        bookDatabaseRepository.setCategories(data.keys)
        bookDatabaseRepository.setBooks(data.values.flatMap { books -> books }.toList())
    }

    fun getBooksAndCategoriesCount(): Pair<Int, Int> {
        val categoriesCount = bookDatabaseRepository.getCategories().count()
        val booksCount = bookDatabaseRepository.getBooks().count()
        return categoriesCount to booksCount
    }

    fun setDatabaseLoaded() {
        settingsRepository.databaseVersion = BooksDatabase.DATABASE_VERSION
    }

    fun setDownloadStatistics(statistics: Pair<Int, Int>) {
        settingsRepository.downloadStatistics = statistics
    }

    fun saveStatisticsData(statisticsData: Map<Category, StatisticsEntity>) {
        val authorStatistics = statisticsData.entries.flatMap { (category, statistics) ->
            statistics.authors.entries.map { (authorName, bookCount) ->
                AuthorStatisticsEntity(category.name, authorName, bookCount)
            }
        }
        statisticDatabaseRepository.setAuthorStatistics(authorStatistics)

        val publisherStatistics = statisticsData.entries.flatMap { (category, statistics) ->
            statistics.publishers.entries.map { (publisher, bookCount) ->
                PublisherStatisticsEntity(category.name, publisher, bookCount)
            }
        }
        statisticDatabaseRepository.setPublisherStatistics(publisherStatistics)

        val statusStatistics = statisticsData.entries.flatMap { (category, statistics) ->
            statistics.statuses.entries.map { (status, bookCount) ->
                StatusStatisticsEntity(category.name, status, bookCount)
            }
        }
        statisticDatabaseRepository.setStatusStatistics(statusStatistics)

        val yearStatistics = statisticsData.entries.flatMap { (category, statistics) ->
            statistics.years.entries.map { (year, bookCount) ->
                YearStatisticsEntity(category.name, year, bookCount)
            }
        }
        statisticDatabaseRepository.setYearStatistics(yearStatistics)

        val priceStatistics = statisticsData.entries.map { (category, statistics) ->
            val (minPrice, maxPrice) = statistics.prices
            PriceStatisticsEntity(category.name, minPrice, maxPrice)
        }
        statisticDatabaseRepository.setPriceStatistics(priceStatistics)
    }

    @Suppress("NON_EXHAUSTIVE_WHEN")
    fun saveInfoData(contactsData: List<Info>) {
        contactsData.forEach {
            when (it.type) {
                Info.InfoType.ORGANIZATION -> infoRepository.setOrganizationName(it.name)

                Info.InfoType.EMAIL -> infoRepository.setContactEmail(it.name)

                Info.InfoType.WEBSITE -> infoRepository.setWebsite(it.name)

                Info.InfoType.FACEBOOK -> infoRepository.setFacebook(it.name)

                Info.InfoType.VK -> infoRepository.setVkGroup(it.name)

                Info.InfoType.WORKING_TIME -> infoRepository.setWorkingTime(it.name)

                Info.InfoType.ADDRESS -> infoRepository.setAddress(it.name)
            }
        }
        contactsData.filter { it.type == Info.InfoType.PHONE }
            .map { it.name }
            .toSet()
            .let { infoRepository.setContactPhones(it) }
    }

    fun saveBannersData(bannersData: List<Banner>) {
        bannersRepository.setBanners(bannersData)
    }

    fun getUnzippedFile(fileId: String): File? =
            localFilesRepository.getUnzippedFile(fileId)
}