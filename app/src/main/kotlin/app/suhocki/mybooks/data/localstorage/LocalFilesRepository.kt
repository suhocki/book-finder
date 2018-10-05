package app.suhocki.mybooks.data.localstorage

import app.suhocki.mybooks.checkThreadInterrupt
import app.suhocki.mybooks.data.parser.XlsParser
import app.suhocki.mybooks.di.DownloadDirectoryPath
import app.suhocki.mybooks.domain.model.XlsDocument
import java.io.*
import java.util.zip.ZipInputStream
import javax.inject.Inject


class LocalFilesRepository @Inject constructor(
    @DownloadDirectoryPath private val downloadDirectoryPath: String,
    private val xlsParser: XlsParser
) {
    fun saveFile(folder: String, fileName: String, data: ByteArray) {
        File(downloadDirectoryPath, folder).mkdirs()
        val file = File("$downloadDirectoryPath/$folder", fileName)
        val bufferedOutputStream: BufferedOutputStream
        val fileOutputStream = FileOutputStream(file)
        bufferedOutputStream = BufferedOutputStream(fileOutputStream)
        bufferedOutputStream.write(data)
        bufferedOutputStream.flush()
        bufferedOutputStream.close()
    }

    fun unzip(fromFile: File): File {
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

    fun parseXlsStructure(xlsFile: File): ArrayList<String> {
        return xlsParser.parseStructure(xlsFile)
    }

    @Suppress("UNNECESSARY_SAFE_CALL")
    fun getDownloadedFile(fileId: String) =
        File(downloadDirectoryPath, fileId)?.let { file ->
            file.walkTopDown().find { !it.isDirectory }
        }

    fun getUnzippedFile(fileId: String) =
        File(downloadDirectoryPath, fileId).walkTopDown()
            .find { it.name.startsWith(UNZIPPED_FILE_PREFIX) }

    fun extractXlsDocument(strings: ArrayList<String>): XlsDocument {
        return xlsParser.extractPayload(strings)
    }


    companion object Constants {
        const val UNZIPPED_FILE_PREFIX = "unzipped_"
    }
}