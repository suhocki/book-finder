package app.suhocki.mybooks.presentation.global

class GlobalAppSettingsController {
    var eventReciever: ((debugPanel: Boolean, adminMode: Boolean) -> Unit)? = null

    fun applySettings(debugPanel: Boolean, adminMode: Boolean) =
        eventReciever?.invoke(debugPanel, adminMode)
}