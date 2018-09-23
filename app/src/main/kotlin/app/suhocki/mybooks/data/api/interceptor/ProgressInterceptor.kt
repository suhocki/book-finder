package app.suhocki.mybooks.data.api.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import app.suhocki.mybooks.data.progress.ProgressHandler
import javax.inject.Inject

class ProgressInterceptor @Inject constructor(
    private val progressEmitter: ProgressHandler
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())
        return originalResponse.newBuilder()
            .body(ProgressResponseBody(originalResponse.body()!!, progressEmitter))
            .build()
    }
}