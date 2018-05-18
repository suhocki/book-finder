package app.suhocki.mybooks.domain

import app.suhocki.mybooks.domain.repository.SettingsRepository
import javax.inject.Inject

class InitialInteractor @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    fun setDatabaseLoaded() {
        settingsRepository.databaseLoaded = true
    }

    fun getDownloadStatistics() =
        settingsRepository.downloadStatistics
}