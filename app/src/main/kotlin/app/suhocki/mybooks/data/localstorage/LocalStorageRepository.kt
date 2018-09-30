package app.suhocki.mybooks.data.localstorage

import app.suhocki.mybooks.checkThreadInterrupt
import app.suhocki.mybooks.data.parser.XlsParser
import app.suhocki.mybooks.di.DownloadDirectoryPath
import app.suhocki.mybooks.domain.model.XlsDocument
import app.suhocki.mybooks.domain.repository.FileActionsRepository
import java.io.*
import java.util.zip.ZipInputStream
import javax.inject.Inject


class LocalStorageRepository @Inject constructor(
    @DownloadDirectoryPath private val downloadDirectoryPath: String,
    private val xlsParser: XlsParser
) : FileActionsRepository {

    override fun saveFile(folder: String, fileName: String, data: ByteArray) {
        File(downloadDirectoryPath, folder).mkdirs()
        val file = File("$downloadDirectoryPath/$folder", fileName)
        val bufferedOutputStream: BufferedOutputStream
        val fileOutputStream = FileOutputStream(file)
        bufferedOutputStream = BufferedOutputStream(fileOutputStream)
        bufferedOutputStream.write(data)
        bufferedOutputStream.flush()
        bufferedOutputStream.close()
    }

    override fun unzip(fromFile: File): File {
        lateinit var outputFile: File
        ZipInputStream(BufferedInputStream(FileInputStream(fromFile))).use { zipInputStream ->
            var count: Int = -1
            val buffer = ByteArray(8192)
            outputFile = File(
                File(downloadDirectoryPath, fromFile.parentFile.name),
                zipInputStream.nextEntry.name
            )
            FileOutputStream(outputFile).use { fileOutputStream ->
                while (zipInputStream.read(buffer).apply { count = this } != -1) {
                    checkThreadInterrupt()
                    fileOutputStream.write(buffer, 0, count)
                }
            }
        }
        return outputFile
    }

    override fun parseXlsStructure(xlsFile: File): ArrayList<String> {
        return xlsParser.parseStructure(xlsFile)
    }

    @Suppress("UNNECESSARY_SAFE_CALL")
    override fun getDownloadedFile(fileId: String) =
        File(downloadDirectoryPath, fileId)?.let {
            it.walkTopDown().find { !it.isDirectory }
        }

    override fun getUnzippedFile(fileId: String) =
        File(downloadDirectoryPath, fileId).walkTopDown()
            .find { it.name.startsWith(FileActionsRepository.Constants.UNZIPPED_FILE_PREFIX) }

    override fun extractXlsDocument(strings: ArrayList<String>): XlsDocument {
        return xlsParser.extractPayload(strings)
    }

}