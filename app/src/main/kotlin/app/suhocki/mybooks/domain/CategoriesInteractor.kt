package app.suhocki.mybooks.domain

import app.suhocki.mybooks.domain.model.Banner
import app.suhocki.mybooks.domain.repository.DatabaseRepository
import app.suhocki.mybooks.domain.repository.SettingsRepository
import app.suhocki.mybooks.presentation.catalog.adapter.model.BannersTypedItem
import app.suhocki.mybooks.presentation.catalog.adapter.model.CategoryTypedItem
import javax.inject.Inject

class CategoriesInteractor @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val settingsRepository: SettingsRepository
) {

    fun getCategories() =
        databaseRepository.getCategories()
            .map { CategoryTypedItem(it) }

    fun getBanners() =
        BannersTypedItem(listOf(object : Banner {
            override val pictureUrl: String
                get() = "https://mybooks.by/pics/items/3_2.jpg"
            override val text: String
                get() = "ТОЛЬКО В КНИГИ МЫ ВЕРИМ"
        }))

    fun setDatabaseLoaded() {
        settingsRepository.databaseLoaded = true
    }
}