package app.suhocki.mybooks.domain

import app.suhocki.mybooks.data.googledrive.RemoteFilesRepository
import app.suhocki.mybooks.data.localstorage.LocalFilesRepository
import app.suhocki.mybooks.data.mapper.Mapper
import app.suhocki.mybooks.di.Room
import app.suhocki.mybooks.domain.model.Banner
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.model.ShopInfo
import app.suhocki.mybooks.domain.model.statistics.*
import app.suhocki.mybooks.domain.repository.BannersRepository
import app.suhocki.mybooks.domain.repository.BooksRepository
import app.suhocki.mybooks.domain.repository.InfoRepository
import app.suhocki.mybooks.domain.repository.StatisticsRepository
import java.io.File
import javax.inject.Inject

class UploadServiceInteractor @Inject constructor(
    @Room private val localBooksRepository: BooksRepository,
    private val remoteFilesRepository: RemoteFilesRepository,
    private val localFilesRepository: LocalFilesRepository,
    private val bannersRepository: BannersRepository,
    private val statisticRepository: StatisticsRepository,
    @Room private val infoRepository: InfoRepository,
    private val mapper: Mapper
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

    fun saveBooksToLocal(data: Map<out Category, Collection<Book>>) {
        localBooksRepository.addCategories(data.keys.toList())
        localBooksRepository.addBooks(data.values.flatMap { books -> books }.toList())
    }

    fun saveStatisticsData(statistics: Map<Category, Statistics>) =
        with(statisticRepository) {
            setAuthorStatistics(mapper.map(statistics, AuthorStatistics::class.java))
            setPublisherStatistics(mapper.map(statistics, PublisherStatistics::class.java))
            setStatusStatistics(mapper.map(statistics, StatusStatistics::class.java))
            setYearStatistics(mapper.map(statistics, YearStatistics::class.java))
            setPriceStatistics(mapper.map(statistics, PriceStatistics::class.java))
        }

    fun saveShopInfo(shopInfo: ShopInfo) =
        infoRepository.setShopInfo(shopInfo)

    fun saveBannersData(bannersData: List<Banner>) {
        bannersRepository.setBanners(bannersData)
    }

    fun getUnzippedFile(fileId: String): File? =
        localFilesRepository.getUnzippedFile(fileId)
}