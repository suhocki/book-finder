package app.suhocki.mybooks.ui.admin.eventbus

data class ProgressEvent(
    var progress: Int,
    val bytes: Long? = null,
    var downloadUrl: String? = null
)