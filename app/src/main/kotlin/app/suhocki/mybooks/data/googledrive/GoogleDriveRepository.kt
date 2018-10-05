package app.suhocki.mybooks.data.googledrive

import app.suhocki.mybooks.data.api.GoogleDriveApi
import app.suhocki.mybooks.data.mapper.Mapper
import app.suhocki.mybooks.domain.model.admin.File
import app.suhocki.mybooks.getResponse
import javax.inject.Inject

class GoogleDriveRepository @Inject constructor(
    private val googleDriveApi: GoogleDriveApi,
    private val mapper: Mapper
) {

    fun getFiles(folderId: String): List<File> {
        val query = "trashed = false and '$folderId' in parents"

        val metaData = googleDriveApi
            .getFolderContents(query, PAGINATION_SIZE)
            .execute().getResponse()

        return metaData.items.map { mapper.map(it, File::class.java) }
    }

    companion object {
        private const val PAGINATION_SIZE = 20
    }
}