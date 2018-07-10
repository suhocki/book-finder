package app.suhocki.mybooks.domain.model

data class License(
    val name: String,
    val url: String,
    val license: LicenseType
) {
    enum class LicenseType {
        MIT,
        APACHE_2,
        CUSTOM,
        NONE
    }
}