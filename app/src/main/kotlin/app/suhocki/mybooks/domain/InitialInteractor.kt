package app.suhocki.mybooks.domain

import app.suhocki.mybooks.domain.repository.SettingsRepository
import javax.inject.Inject

class InitialInteractor @Inject constructor(
    private val settingsRepository: SettingsRepository
) {

    fun getDownloadStatistics() =
        settingsRepository.downloadStatistics

    fun setDatabaseVersion(version: Int) {
        settingsRepository.databaseVersion = version
    }
}