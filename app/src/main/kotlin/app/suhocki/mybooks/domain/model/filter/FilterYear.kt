package app.suhocki.mybooks.domain.model.filter

interface FilterYear {
    val year: String
    val booksCount: Int
    var isChecked: Boolean
}