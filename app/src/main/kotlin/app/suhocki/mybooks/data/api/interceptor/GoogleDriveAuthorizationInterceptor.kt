package app.suhocki.mybooks.data.api.interceptor

import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.resources.ResourceManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class GoogleDriveAuthorizationInterceptor @Inject constructor(
    private val resourceManager: ResourceManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val apiKey = resourceManager.getString(R.string.google_drive_api_key)

        val httpUrl = original.url().newBuilder()
            .addQueryParameter(PARAMETER_KEY, apiKey)
            .build()

        return chain.proceed(original.newBuilder()
            .url(httpUrl)
            .build())
    }


    companion object {
        private const val PARAMETER_KEY = "key"
    }
}