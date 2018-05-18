package app.suhocki.mybooks.domain

import app.suhocki.mybooks.domain.repository.DatabaseRepository
import app.suhocki.mybooks.domain.repository.SettingsRepository
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