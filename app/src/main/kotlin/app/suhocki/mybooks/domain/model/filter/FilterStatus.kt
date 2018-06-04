package app.suhocki.mybooks.domain.model.filter

interface FilterStatus {
    val status: String
    val booksCount: Int
    var isChecked: Boolean
}