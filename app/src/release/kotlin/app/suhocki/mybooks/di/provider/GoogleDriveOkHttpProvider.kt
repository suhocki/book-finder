package app.suhocki.mybooks.di.provider

import okhttp3.OkHttpClient
import app.suhocki.mybooks.data.api.interceptor.GoogleDriveAuthorizationInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider
import io.fabric.sdk.android.services.settings.IconRequest.build
import app.suhocki.mybooks.data.api.interceptor.ProgressInterceptor


class GoogleDriveOkHttpProvider @Inject constructor(
    private val googleDriveAuthorizationInterceptor: GoogleDriveAuthorizationInterceptor
) : Provider<OkHttpClient> {

    override fun get(): OkHttpClient =
        OkHttpClient.Builder()
            .readTimeout(7, TimeUnit.SECONDS)
            .addInterceptor(googleDriveAuthorizationInterceptor)
            .build()
}