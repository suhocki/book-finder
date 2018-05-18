package app.suhocki.mybooks.domain.repository

interface ServerRepository {
    fun getFile(fileUrl: String): ByteArray
}