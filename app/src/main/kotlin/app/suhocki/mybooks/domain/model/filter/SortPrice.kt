package app.suhocki.mybooks.domain.model.filter

interface SortPrice : Checkable {
    val sortName: String
    var groupItem: SortPrice?
}