package app.suhocki.mybooks.ui.admin.eventbus

data class ProgressEvent(
    var progress: Int? = null,
    val bytes: Long? = null
)