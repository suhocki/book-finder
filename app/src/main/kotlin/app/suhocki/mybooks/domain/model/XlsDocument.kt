package app.suhocki.mybooks.domain.model

interface XlsDocument {
    val title: String
    val creationDate: String
    val columnNames: List<String>
    val data: Map<out Category, Collection<Book>>
}