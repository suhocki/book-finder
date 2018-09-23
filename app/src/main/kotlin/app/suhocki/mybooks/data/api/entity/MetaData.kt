package app.suhocki.mybooks.data.api.entity

data class MetaData(
    val items: List<Item>
) {
    data class Item(
        val title: String,
        val id: String
    )
}