package app.suhocki.mybooks.data.repository

import app.suhocki.mybooks.checkThreadInterrupt
import app.suhocki.mybooks.data.parser.XlsParser
import app.suhocki.mybooks.di.DownloadDirectoryPath
import app.suhocki.mybooks.domain.model.XlsDocument
import app.suhocki.mybooks.domain.repository.FileSystemRepository
import java.io.*
import java.util.*
import java.util.zip.ZipInputStream
import javax.inject.Inject


class LocalStorageRepository @Inject constructor(
    @DownloadDirectoryPath private val downloadDirectoryPath: String,
    private val xlsParser: XlsParser
) : FileSystemRepository {

    override fun saveFile(fileName: String, data: ByteArray): File {
        val file = File(downloadDirectoryPath, fileName)
        val bufferedOutputStream: BufferedOutputStream
        val fileOutputStream = FileOutputStream(file)
        bufferedOutputStream = BufferedOutputStream(fileOutputStream)
        bufferedOutputStream.write(data)
        bufferedOutputStream.flush()
        bufferedOutputStream.close()
        return file
    }

    override fun unzip(fromFile: File, toDirectory: File): File {
        lateinit var outputFile: File
        ZipInputStream(BufferedInputStream(FileInputStream(fromFile))).use { zipInputStream ->
            var count: Int = -1
            val buffer = ByteArray(8192)
            outputFile = File(toDirectory, zipInputStream.nextEntry.name)
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

    override fun extractXlsDocument(strings: ArrayList<String>): XlsDocument {
        return xlsParser.extractPayload(strings)
    }
}