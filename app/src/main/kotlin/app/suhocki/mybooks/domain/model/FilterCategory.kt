package app.suhocki.mybooks.domain.model

interface FilterCategory {
    val title: String
    var isExpanded: Boolean
    var isConfigurated: Boolean
}