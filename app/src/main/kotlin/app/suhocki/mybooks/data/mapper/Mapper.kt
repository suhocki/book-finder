package app.suhocki.mybooks.data.mapper

import app.suhocki.mybooks.data.mapper.converter.MetaDataItemToFile
import app.suhocki.mybooks.data.mapper.converter.StringToResponseKind
import javax.inject.Inject

class Mapper @Inject constructor(
    metaDataItemToFile: MetaDataItemToFile,
    stringToResponseKind: StringToResponseKind
) {
    private val converters by lazy {
        mutableListOf<Converter<*, *>>(
            metaDataItemToFile,
            stringToResponseKind
        )
    }

    fun <FROM : Any, TO> map(value: FROM, toClass: Class<TO>): TO {

        @Suppress("UNCHECKED_CAST")
        val converter = converters.find {
            it.fromClass == value.javaClass && it.toClass == toClass
        } as Converter<FROM, TO>

        return converter.convert(value)
    }
}