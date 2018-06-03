package app.suhocki.mybooks.domain

import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.repository.BookDatabaseRepository
import javax.inject.Inject

class BooksInteractor @Inject constructor(
    private val databaseRepository: BookDatabaseRepository
) {

    fun getBooks(category: Category) =
        databaseRepository.getBooksFor(category)
}