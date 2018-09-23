package app.suhocki.mybooks.di.provider

import okhttp3.OkHttpClient
import app.suhocki.mybooks.data.api.interceptor.GoogleDriveAuthorizationInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider
import com.facebook.stetho.okhttp3.StethoInterceptor
import io.fabric.sdk.android.services.settings.IconRequest.build



class GoogleDriveOkHttpProvider @Inject constructor(
    private val googleDriveAuthorizationInterceptor: GoogleDriveAuthorizationInterceptor
): Provider<OkHttpClient> {

    override fun get(): OkHttpClient =
        OkHttpClient.Builder()
            .readTimeout(7, TimeUnit.SECONDS)
            .addInterceptor(googleDriveAuthorizationInterceptor)
            .addNetworkInterceptor(StethoInterceptor())
            .build()
}