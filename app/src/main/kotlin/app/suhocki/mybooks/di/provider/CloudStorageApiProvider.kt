package app.suhocki.mybooks.di.provider

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import app.suhocki.mybooks.data.api.CloudStorageApi
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Provider

class CloudStorageApiProvider @Inject constructor(
    private val okHttpClient: OkHttpClient
): Provider<CloudStorageApi> {

    override fun get(): CloudStorageApi =
            Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(RANDOM_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CloudStorageApi::class.java)

    companion object {
        private const val RANDOM_URL = "https://vk.com/feed/"
    }
}