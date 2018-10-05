package app.suhocki.mybooks.domain

import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.googledrive.GoogleDriveRepository
import app.suhocki.mybooks.data.resources.ResourceManager
import javax.inject.Inject

class AdminInteractor @Inject constructor(
    private val googleDriveRepository: GoogleDriveRepository,
    private val resourceManager: ResourceManager
) {
    fun getAvailableFiles() =
        googleDriveRepository.getFiles(resourceManager.getString(R.string.google_drive_folder_id))
}