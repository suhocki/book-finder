package suhockii.dev.bookfinder.domain.repository

interface SettingsRepository {
    var databaseLoaded: Boolean

    var downloadStatistics: Pair<Int, Int>?
}