package app.suhocki.mybooks.domain.repository

interface CloudStorageRepository {
    fun getFile(fileUrl: String): ByteArray
}