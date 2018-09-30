package app.suhocki.mybooks.data.mapper

import app.suhocki.mybooks.data.mapper.converter.MetaDataItemToFile
import java.util.*
import javax.inject.Inject

class Mapper @Inject constructor(
    locale: Locale
) {
    private val converters = mutableListOf<Converter<*, *>>(
        MetaDataItemToFile(locale)
    )

    fun <FROM : Any, TO> map(value: FROM, toClass: Class<TO>): TO {

        @Suppress("UNCHECKED_CAST")
        val converter = converters.find {
            it.fromClass == value.javaClass && it.toClass == toClass
        } as Converter<FROM, TO>

        return converter.convert(value)
    }
}