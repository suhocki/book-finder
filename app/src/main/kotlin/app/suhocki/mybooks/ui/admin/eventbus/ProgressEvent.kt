package app.suhocki.mybooks.ui.admin.eventbus

import java.io.Serializable

data class ProgressEvent(
    var progress: Int,
    val bytes: Long? = null,
    var downloadUrl: String? = null
) : Serializable