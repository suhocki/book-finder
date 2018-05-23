package app.suhocki.mybooks.domain

import app.suhocki.mybooks.di.DatabaseFileUrl
import app.suhocki.mybooks.di.DownloadedFileName
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.repository.DatabaseRepository
import app.suhocki.mybooks.domain.repository.FileSystemRepository
import app.suhocki.mybooks.domain.repository.ServerRepository
import app.suhocki.mybooks.domain.repository.SettingsRepository
import java.io.File
import javax.inject.Inject

class BackgroundInteractor @Inject constructor(
    private val serverRepository: ServerRepository,
    private val fileSystemRepository: FileSystemRepository,
    private val databaseRepository: DatabaseRepository,
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

    fun saveDocumentData(data: Map<out Category, Collection<Book>>) {
        databaseRepository.saveCategories(data.keys)
        databaseRepository.saveBooks(data.values.flatMap { books -> books }.toList())
    }

    fun getBooksAndCategoriesCount(): Pair<Int, Int> {
        val categoriesCount = databaseRepository.getCategories().count()
        val booksCount = databaseRepository.getBooks().count()
        return categoriesCount to booksCount
    }

    fun setDatabaseLoaded() {
        settingsRepository.databaseLoaded = true
    }

    fun setDownloadStatistics(statistics: Pair<Int, Int>) {
        settingsRepository.downloadStatistics = statistics
    }
}