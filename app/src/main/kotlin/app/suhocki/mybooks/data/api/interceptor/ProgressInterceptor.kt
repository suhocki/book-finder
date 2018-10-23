package app.suhocki.mybooks.data.api.interceptor

import app.suhocki.mybooks.data.notification.NotificationHelper
import app.suhocki.mybooks.di.module.UploadServiceModule
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ProgressInterceptor @Inject constructor(
    private val uploadControl: UploadServiceModule.UploadControlEntity,
    private val notificationHelper: NotificationHelper
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