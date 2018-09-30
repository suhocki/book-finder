package app.suhocki.mybooks.data.mapper.converter

import app.suhocki.mybooks.data.api.entity.MetaData
import app.suhocki.mybooks.data.mapper.BaseConverter
import app.suhocki.mybooks.domain.model.admin.File
import app.suhocki.mybooks.toHumanDate
import app.suhocki.mybooks.toHumanFileSize
import java.util.*

class MetaDataItemToFile(
    private val locale: Locale
) : BaseConverter<MetaData.Item, File>(
    MetaData.Item::class.java,
    File::class.java
) {
    override fun convert(value: MetaData.Item) =
        FileEntity(
            value.title,
            value.id,
            value.date.toHumanDate(locale),
            value.size.toHumanFileSize(),
            value.size
        )

    class FileEntity(
        override val name: String,
        override val id: String,
        override val date: String,
        override val humanFileSize: String,
        override val fileSize: Long
    ) : File
}