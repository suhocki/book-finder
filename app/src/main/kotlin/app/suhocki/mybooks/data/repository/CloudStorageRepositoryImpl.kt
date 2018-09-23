package app.suhocki.mybooks.data.repository

import app.suhocki.mybooks.data.api.CloudStorageApi
import app.suhocki.mybooks.domain.repository.CloudStorageRepository
import javax.inject.Inject

class CloudStorageRepositoryImpl @Inject constructor(
    private val cloudStorageApi: CloudStorageApi
) : CloudStorageRepository {

    override fun getFile(fileUrl: String): ByteArray {
        val response = cloudStorageApi.getFile(fileUrl).execute()
        return response.body()!!.bytes()
    }
}