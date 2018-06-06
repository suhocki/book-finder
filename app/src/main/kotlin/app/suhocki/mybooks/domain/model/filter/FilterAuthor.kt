package app.suhocki.mybooks.domain.model.filter

interface FilterAuthor {
    val authorName: String
    val booksCount: Int
    var isChecked: Boolean
    var isCheckable: Boolean
}