package app.suhocki.mybooks.data.googledrive

import app.suhocki.mybooks.data.api.FilesApi
import app.suhocki.mybooks.getResponse
import javax.inject.Inject

class RemoteFilesRepository @Inject constructor(
    private val filesApi: FilesApi
) {

    fun downloadFile(fileId: String): ByteArray {
        return filesApi.downloadFile(fileId, EXPORT_TYPE)
            .execute().getResponse().bytes()!!
    }


    companion object {
        private const val EXPORT_TYPE = "download"
    }
}