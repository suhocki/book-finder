package app.suhocki.mybooks.data.mapper.converter

import app.suhocki.mybooks.data.api.entity.MetaData
import app.suhocki.mybooks.data.mapper.BaseConverter
import app.suhocki.mybooks.domain.model.admin.File

class MetaDataItemToFile : BaseConverter<MetaData.Item, File>(
    MetaData.Item::class.java,
    File::class.java
) {
    override fun convert(value: MetaData.Item) =
        FileEntity(value.title, value.id)

    class FileEntity(
        override val name: String,
        override val id: String
    ) : File
}