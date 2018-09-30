package app.suhocki.mybooks.domain.repository

import app.suhocki.mybooks.domain.model.XlsDocument
import com.google.common.primitives.Bytes
import java.io.File

interface FileActionsRepository {

    fun saveFile(folder: String, fileName: String, data: ByteArray)

    fun unzip(fromFile: File): File

    fun parseXlsStructure(xlsFile: File): ArrayList<String>

    fun extractXlsDocument(strings: ArrayList<String>): XlsDocument

    fun getDownloadedFile(fileId: String): File?

    fun getUnzippedFile(fileId: String): File?


    object Constants {
        const val UNZIPPED_FILE_PREFIX = "unzipped_"
    }
}