package app.suhocki.mybooks.domain.model

interface SearchResult {
    val foundBy: String
    val book: Book
}