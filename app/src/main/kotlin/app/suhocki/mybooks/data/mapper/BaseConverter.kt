package app.suhocki.mybooks.data.mapper

abstract class BaseConverter<FROM, TO>(
    override val fromClass: Class<FROM>,
    override val toClass: Class<TO>
) : Converter<FROM, TO>