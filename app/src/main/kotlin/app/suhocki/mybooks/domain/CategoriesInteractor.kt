package app.suhocki.mybooks.domain

import app.suhocki.mybooks.domain.model.Banner
import app.suhocki.mybooks.domain.repository.DatabaseRepository
import javax.inject.Inject

class CategoriesInteractor @Inject constructor(
    private val databaseRepository: DatabaseRepository
) {

    fun getCategories() =
        databaseRepository.getCategories()

    fun getBanner() =
        object: Banner {
            override val description: String
                get() = "ТОЛЬКО В КНИГИ МЫ ВЕРИМ"

            override val imageUrl: String
                get() = "https://mybooks.by/pics/items/3_2.jpg"
        }
}