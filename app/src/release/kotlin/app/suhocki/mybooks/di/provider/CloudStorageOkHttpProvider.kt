package app.suhocki.mybooks.di.provider

import okhttp3.OkHttpClient
import app.suhocki.mybooks.data.api.interceptor.ProgressInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider

class CloudStorageOkHttpProvider @Inject constructor(
    private val progressInterceptor: ProgressInterceptor
): Provider<OkHttpClient> {

    override fun get(): OkHttpClient =
        OkHttpClient.Builder()
            .readTimeout(7, TimeUnit.SECONDS)
            .addInterceptor(progressInterceptor)
            .build()
}