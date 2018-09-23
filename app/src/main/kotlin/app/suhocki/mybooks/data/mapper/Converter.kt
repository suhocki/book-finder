package app.suhocki.mybooks.data.mapper

interface Converter<FROM, TO>  {

    fun convert(value: FROM): TO

    val fromClass: Class<FROM>

    val toClass: Class<TO>
}