package app.suhocki.mybooks.data.api.interceptor

import app.suhocki.mybooks.data.notification.ForegroundNotificationHelper
import app.suhocki.mybooks.ui.base.entity.UploadControlEntity
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ProgressInterceptor @Inject constructor(
    private val uploadControl: UploadControlEntity,
    private val notificationHelper: ForegroundNotificationHelper
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())

        val contentLength =
            originalResponse.networkResponse()?.header(HEADER_CONTENT_LENGTH, null)?.toLong()

        val originalResponseBody = originalResponse.body()!!

        val progressResponseBody = ProgressResponseBody(
            contentLength, originalResponseBody, uploadControl, notificationHelper
        )

        return originalResponse.newBuilder()
            .body(progressResponseBody)
            .build()
    }


    companion object {
        private const val HEADER_CONTENT_LENGTH = "content-length"
    }
}