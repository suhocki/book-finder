package app.suhocki.mybooks.domain.repository

import app.suhocki.mybooks.domain.model.admin.File

interface GoogleDriveRepository {
    fun getFiles(folderId: String): List<File>

    fun downloadFile(fileId: String): ByteArray
}