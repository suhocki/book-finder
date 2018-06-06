package app.suhocki.mybooks.domain.model.filter

interface SortPrice {
    val sortName: String
    var isChecked: Boolean
    var groupItem: SortPrice?
}