package app.suhocki.mybooks.domain.model.filter

interface SortName : Checkable {
    val sortName: String
    var groupItem: SortName?
}