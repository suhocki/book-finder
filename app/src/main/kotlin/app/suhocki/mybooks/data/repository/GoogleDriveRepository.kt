package app.suhocki.mybooks.data.repository

import app.suhocki.mybooks.data.network.CloudStorageApi
import app.suhocki.mybooks.domain.repository.ServerRepository
import javax.inject.Inject

class GoogleDriveRepository @Inject constructor(
    private val googleDriveApi: CloudStorageApi
) : ServerRepository {

    override fun getFile(fileUrl: String): ByteArray {
        val response = googleDriveApi.getFile(fileUrl).execute()
        return response.body()!!.bytes()
    }
}