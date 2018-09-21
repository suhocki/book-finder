package app.suhocki.mybooks.domain.repository

interface SettingsRepository {
    var databaseVersion: Int

    var downloadStatistics: Pair<Int, Int>?

    var isAdminModeEnabled: Boolean
}