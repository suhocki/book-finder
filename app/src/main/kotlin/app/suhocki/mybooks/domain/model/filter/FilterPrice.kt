package app.suhocki.mybooks.domain.model.filter

interface FilterPrice {
    val hintFrom: Double
    val hintTo: Double
    var from: Double
    var to: Double
}