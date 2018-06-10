package app.suhocki.mybooks.domain

import android.arch.persistence.db.SupportSQLiteQuery
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.repository.BooksRepository
import javax.inject.Inject

class BooksInteractor @Inject constructor(
    private val databaseRepository: BooksRepository
) {

    fun getBooks(category: Category) =
        databaseRepository.getBooksFor(category)

    fun filter(sqLiteQuery: SupportSQLiteQuery) =
        databaseRepository.filter(sqLiteQuery)
}