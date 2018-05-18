package app.suhocki.mybooks.domain.repository

interface SettingsRepository {
    var databaseLoaded: Boolean

    var downloadStatistics: Pair<Int, Int>?
}