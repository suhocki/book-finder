package suhockii.dev.bookfinder.data.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import suhockii.dev.bookfinder.data.progress.ProgressHandler
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