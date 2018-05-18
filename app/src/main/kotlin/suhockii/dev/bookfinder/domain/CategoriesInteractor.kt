package suhockii.dev.bookfinder.domain

import suhockii.dev.bookfinder.domain.repository.DatabaseRepository
import suhockii.dev.bookfinder.domain.repository.SettingsRepository
import javax.inject.Inject

class CategoriesInteractor @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val settingsRepository: SettingsRepository
) {

    fun getCategories() =
        databaseRepository.getCategories()

    fun setDatabaseLoaded() {
        settingsRepository.databaseLoaded = true
    }
}