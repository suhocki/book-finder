package app.suhocki.mybooks.ui.licenses.listener

import app.suhocki.mybooks.domain.model.License

interface OnLicenseClickListener {
    fun onLicenseClick(license: License)
}
