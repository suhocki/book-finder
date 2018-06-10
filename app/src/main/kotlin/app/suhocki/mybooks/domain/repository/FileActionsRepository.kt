package app.suhocki.mybooks.domain.repository

import app.suhocki.mybooks.domain.model.XlsDocument
import java.io.File

interface FileActionsRepository {
    fun saveFile(fileName: String, data: ByteArray): File

    fun unzip(fromFile: File, toDirectory: File): File

    fun parseXlsStructure(xlsFile: File): ArrayList<String>

    fun extractXlsDocument(strings: ArrayList<String>): XlsDocument
}