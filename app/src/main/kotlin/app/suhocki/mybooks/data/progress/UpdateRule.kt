package app.suhocki.mybooks.data.progress

internal class UpdateRule(
    private val updateInterval: Long,
    var lastUpdate: Long = 0
) {
    fun requestUpdate(): Boolean {
        val currentTimeMillis = System.currentTimeMillis()
        val timeAfterLastUpdate = currentTimeMillis - lastUpdate
        val canUpdate = timeAfterLastUpdate >= updateInterval
        if (canUpdate) lastUpdate = currentTimeMillis
        return canUpdate
    }
}