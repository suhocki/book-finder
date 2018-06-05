package app.suhocki.mybooks.domain.model.filter

interface FilterPrice {
    val from: Double
    val to: Double
    val hintFrom: String
    val hintTo: String
}