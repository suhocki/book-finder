package app.suhocki.mybooks.domain

import app.suhocki.mybooks.domain.repository.FilterRepository
import javax.inject.Inject

class FilterInteractor @Inject constructor(
    private val filterRepository: FilterRepository
) {

    fun getFilterCategories() =
        filterRepository.getFilterCategories()
}