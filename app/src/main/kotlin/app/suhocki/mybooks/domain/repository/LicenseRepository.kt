package app.suhocki.mybooks.domain.repository

import app.suhocki.mybooks.domain.model.License

interface LicenseRepository {
    fun getLicenses(): List<License>
}