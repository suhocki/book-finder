package app.suhocki.mybooks.data.api.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ProgressInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())

        val contentLength =
            originalResponse.networkResponse()?.header(HEADER_CONTENT_LENGTH, null)?.toLong()

        val originalResponseBody = originalResponse.body()!!
        val url = originalResponse.request().url().toString()

        val progressResponseBody = ProgressResponseBody(url, contentLength, originalResponseBody)

        return originalResponse.newBuilder()
            .body(progressResponseBody)
            .build()
    }


    companion object {
        private const val HEADER_CONTENT_LENGTH = "content-length"
    }
}