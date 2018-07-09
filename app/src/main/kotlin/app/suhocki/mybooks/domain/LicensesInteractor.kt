package app.suhocki.mybooks.domain

import app.suhocki.mybooks.domain.repository.LicenseRepository
import javax.inject.Inject

class LicensesInteractor @Inject constructor(
    private val licensesRepository: LicenseRepository
) {

    fun getLicenses() =
        licensesRepository.getLicenses()
}