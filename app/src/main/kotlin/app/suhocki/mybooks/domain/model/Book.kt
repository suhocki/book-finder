package app.suhocki.mybooks.domain.model

interface Book {
    val id: String
    val categoryId: String
    val shortName: String
    val fullName: String
    val price: Double
    val iconLink: String
    val productLink: String
    val website: String
    val status: String?
    val publisher: String?
    val author: String?
    val series: String?
    val format: String?
    val year: String?
    val pageCount: String?
    val cover: String?
    val description: String?
}