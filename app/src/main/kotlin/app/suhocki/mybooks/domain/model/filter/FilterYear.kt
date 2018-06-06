package app.suhocki.mybooks.domain.model.filter

interface FilterYear :  Checkable {
    val year: String
    val booksCount: Int
}