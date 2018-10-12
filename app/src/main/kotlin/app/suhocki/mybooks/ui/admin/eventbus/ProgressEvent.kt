package app.suhocki.mybooks.ui.admin.eventbus

import java.io.Serializable

data class ProgressEvent(
    var progress: Int,
    var bytes: Long? = null
) : Serializable