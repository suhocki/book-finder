package app.suhocki.mybooks.ui.admin.eventbus

import java.io.Serializable

data class UploadCompleteEvent(
    val shouldKillService: Boolean
) : Serializable