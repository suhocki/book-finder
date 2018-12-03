package app.suhocki.mybooks.data.mapper

import app.suhocki.mybooks.di.Converters
import javax.inject.Inject

class Mapper @Inject constructor(
    @Converters inline val converters: Set<Converter<*, *>>
) {
    @Suppress("UNCHECKED_CAST")
    inline fun <reified To> map(input: Any, genericType: Class<out Any>? = null): To {
        if (input is To) return input

        val converter = findConverter<To>(input, genericType)
            ?: throw NoSuchElementException("Cannot find converter from ${input::class.java} to ${To::class.java}")

        return (converter as Converter<Any, To>).convert(input)
    }

    inline fun <reified To> findConverter(
        input: Any,
        genericType: Class<out Any>?
    ) = converters.find {
        val isSuitableConverter =
            it.fromClass.isAssignableFrom(input::class.java) &&
                    To::class.java.isAssignableFrom(it.toClass)

        if (genericType != null)
            it is GenericConverter && isSuitableConverter &&
                    it.genericType == genericType
        else
            isSuitableConverter
    }
}