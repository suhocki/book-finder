package app.suhocki.mybooks.data.mapper

abstract class GenericConverter<FROM, TO>(
    fromClass: Class<FROM>,
    toClass: Class<TO>
) : BaseConverter<FROM, TO>(fromClass, toClass) {

    abstract val genericType: Class<out Any>
}