package app.suhocki.mybooks.ui.admin.eventbus

import java.io.Serializable

data class ServiceKilledEvent(
    val shouldKillService: Boolean
) : Serializable