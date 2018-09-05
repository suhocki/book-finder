package app.suhocki.mybooks.domain.model

data class Changelog (
    val version: String,
    val date: Long,
    val link: String?,
    val changes: Array<String>
)