package app.suhocki.mybooks.data.api.entity

import com.google.gson.annotations.SerializedName

data class MetaData(
    @SerializedName("items") val items: List<Item>
) {
    data class Item(
        @SerializedName("title") val title: String,
        @SerializedName("fileSize") val size: Long,
        @SerializedName("id") val id: String,
        @SerializedName("modifiedDate") val date: String
    )
}