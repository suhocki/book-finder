package app.suhocki.mybooks.domain

import app.suhocki.mybooks.BuildConfig
import app.suhocki.mybooks.domain.repository.GoogleDriveRepository
import javax.inject.Inject

class AdminInteractor @Inject constructor(
    private val googleDriveRepository: GoogleDriveRepository
) {
    fun getAvailableFiles() =
        googleDriveRepository.getFiles(BuildConfig.DATABASES_FOLDER_ID)
}