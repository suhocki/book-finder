package app.suhocki.mybooks.domain.model.statistics

interface Statistics {
    val authors: Map<String, Int>
    val publishers: Map<String, Int>
    val years: Map<String, Int>
    val statuses: Map<String, Int>
}