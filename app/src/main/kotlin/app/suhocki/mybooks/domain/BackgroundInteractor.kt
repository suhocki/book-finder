package app.suhocki.mybooks.domain

import app.suhocki.mybooks.data.database.entity.*
import app.suhocki.mybooks.data.parser.entity.StatisticsEntity
import app.suhocki.mybooks.di.DatabaseFileUrl
import app.suhocki.mybooks.di.DownloadedFileName
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.repository.*
import java.io.File
import javax.inject.Inject

class BackgroundInteractor @Inject constructor(
    private val serverRepository: ServerRepository,
    private val fileSystemRepository: FileSystemRepository,
    private val bookDatabaseRepository: BookDatabaseRepository,
    private val statisticDatabaseRepository: StatisticDatabaseRepository,
    private val settingsRepository: SettingsRepository,
    @DatabaseFileUrl private val fileUrl: String,
    @DownloadedFileName private val downloadedFileName: String
) {
    fun downloadDatabaseFile() =
        serverRepository.getFile(fileUrl)

    fun saveDatabaseFile(bytes: ByteArray) =
        fileSystemRepository.saveFile(downloadedFileName, bytes)

    fun unzip(fromFile: File, toDirectory: File) =
        fileSystemRepository.unzip(fromFile, toDirectory)

    fun parseXlsStructure(xlsFile: File) =
        fileSystemRepository.parseXlsStructure(xlsFile)

    fun extractXlsDocument(strings: ArrayList<String>) =
        fileSystemRepository.extractXlsDocument(strings)

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
        settingsRepository.databaseLoaded = true
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
}