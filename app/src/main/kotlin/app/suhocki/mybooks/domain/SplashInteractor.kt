package app.suhocki.mybooks.domain

import app.suhocki.mybooks.domain.repository.SettingsRepository
import javax.inject.Inject

class SplashInteractor @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    fun isDataLoaded() =
        settingsRepository.databaseLoaded
}