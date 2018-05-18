package app.suhocki.mybooks.domain

import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.repository.DatabaseRepository
import javax.inject.Inject

class BooksInteractor @Inject constructor(
    private val databaseRepository: DatabaseRepository
) {

    fun getBooks(category: Category) =
        databaseRepository.getBooksBy(category)
}