package app.suhocki.mybooks.domain

import app.suhocki.mybooks.domain.repository.SettingsRepository
import javax.inject.Inject

class MainInteractor @Inject constructor(
    private val settingsRepository: SettingsRepository
) {

    fun toogleAdminMode(): Boolean {
        settingsRepository.isAdminModeEnabled = !settingsRepository.isAdminModeEnabled
        return settingsRepository.isAdminModeEnabled
    }

    fun isAdminModeEnabled() =
        settingsRepository.isAdminModeEnabled
}