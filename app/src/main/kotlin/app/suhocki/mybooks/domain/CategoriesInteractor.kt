package app.suhocki.mybooks.domain

import app.suhocki.mybooks.domain.repository.DatabaseRepository
import app.suhocki.mybooks.domain.repository.SettingsRepository
import app.suhocki.mybooks.presentation.categories.adapter.model.CategoryTypedItem
import javax.inject.Inject

class CategoriesInteractor @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val settingsRepository: SettingsRepository
) {

    fun getCategories() =
        databaseRepository.getCategories()
            .map { CategoryTypedItem(it) }

    fun setDatabaseLoaded() {
        settingsRepository.databaseLoaded = true
    }
}