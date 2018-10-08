package app.suhocki.mybooks.di.provider

import app.suhocki.mybooks.data.api.interceptor.ProgressInterceptor
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider


class FilesOkHttpProvider @Inject constructor(
    private val progressInterceptor: ProgressInterceptor
) : Provider<OkHttpClient> {

    override fun get(): OkHttpClient =
        OkHttpClient.Builder()
            .readTimeout(7, TimeUnit.SECONDS)
            .addInterceptor(progressInterceptor)
            .addNetworkInterceptor(StethoInterceptor())
            .build()
}