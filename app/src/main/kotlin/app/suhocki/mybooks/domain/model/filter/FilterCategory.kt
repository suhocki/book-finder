package app.suhocki.mybooks.domain.model.filter

interface FilterCategory {
    val title: String
    var isExpanded: Boolean
    var checkedCount: Int
}