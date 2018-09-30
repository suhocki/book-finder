package app.suhocki.mybooks.data.googledrive

import app.suhocki.mybooks.BuildConfig
import app.suhocki.mybooks.data.api.GoogleDriveApi
import app.suhocki.mybooks.data.mapper.Mapper
import app.suhocki.mybooks.domain.model.admin.File
import app.suhocki.mybooks.domain.repository.GoogleDriveRepository
import app.suhocki.mybooks.getResponse
import com.google.android.gms.common.util.Strings
import javax.inject.Inject

class GoogleDriveRepositoryImpl @Inject constructor(
    private val googleDriveApi: GoogleDriveApi,
    private val mapper: Mapper
) : GoogleDriveRepository {

    override fun getFiles(folderId: String): List<File> {
        val query = "trashed = false and '$folderId' in parents"

        val metaData = googleDriveApi
            .getFolderContents(query, PAGINATION_SIZE)
            .execute().getResponse()

        return metaData.items.map { mapper.map(it, File::class.java) }
    }

    override fun downloadFile(fileId: String): ByteArray {
        val fileUrl = String.format(BuildConfig.GOOGLE_DRIVE_FILE_DOWNLOAD_URL, fileId)
        return googleDriveApi.downloadFile(fileUrl)
            .execute().getResponse().bytes()!!
    }


    companion object {
        private const val PAGINATION_SIZE = 20
    }
}