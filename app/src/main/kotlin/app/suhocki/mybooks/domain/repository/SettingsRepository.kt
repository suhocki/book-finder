package app.suhocki.mybooks.domain.repository

interface SettingsRepository {
    var isAdminModeEnabled: Boolean
    var updateDate: String
}