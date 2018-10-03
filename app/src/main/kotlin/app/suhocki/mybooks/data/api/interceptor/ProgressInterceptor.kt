package app.suhocki.mybooks.data.api.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import app.suhocki.mybooks.data.progress.ProgressHandler
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import javax.inject.Inject

class ProgressInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())

        val contentLength =
            originalResponse.networkResponse()?.header(HEADER_CONTENT_LENGTH, null)?.toLong()

        val responseBody = originalResponse.body()!!
        val url = originalResponse.request().url().toString()

        return originalResponse.newBuilder()
            .body(ProgressResponseBody(url, contentLength, responseBody))
            .build()
    }


    companion object {
        private const val HEADER_CONTENT_LENGTH = "content-length"
    }
}