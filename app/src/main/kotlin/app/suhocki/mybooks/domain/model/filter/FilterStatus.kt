package app.suhocki.mybooks.domain.model.filter

interface FilterStatus : Checkable{
    val status: String
    val booksCount: Int
}