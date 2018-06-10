package app.suhocki.mybooks.di.provider

import okhttp3.OkHttpClient
import app.suhocki.mybooks.data.network.interceptor.ProgressInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider
import com.facebook.stetho.okhttp3.StethoInterceptor
import io.fabric.sdk.android.services.settings.IconRequest.build



class OkHttpClientProvider @Inject constructor(
    private val progressInterceptor: ProgressInterceptor
): Provider<OkHttpClient> {

    override fun get(): OkHttpClient =
        OkHttpClient.Builder()
            .readTimeout(7, TimeUnit.SECONDS)
            .addInterceptor(progressInterceptor)
            .addNetworkInterceptor(StethoInterceptor())
            .build()
}