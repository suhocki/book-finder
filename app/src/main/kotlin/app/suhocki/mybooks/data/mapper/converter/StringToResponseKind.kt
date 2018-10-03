package app.suhocki.mybooks.data.mapper.converter

import app.suhocki.mybooks.BuildConfig
import app.suhocki.mybooks.data.mapper.BaseConverter
import app.suhocki.mybooks.domain.model.ResponseKind
import javax.inject.Inject

class StringToResponseKind @Inject constructor() : BaseConverter<String, ResponseKind>(
    String::class.java,
    ResponseKind::class.java
) {
    override fun convert(value: String) =
        when {
            value.startsWith(BuildConfig.GOOGLE_DRIVE_API_URL) -> ResponseKind.JSON

            else -> ResponseKind.FILE
        }
}