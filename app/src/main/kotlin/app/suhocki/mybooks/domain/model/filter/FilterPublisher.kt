package app.suhocki.mybooks.domain.model.filter

interface FilterPublisher {
    val publisherName: String
    val booksCount: Int
    var isChecked: Boolean
}