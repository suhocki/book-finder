package app.suhocki.mybooks.di.provider

import app.suhocki.mybooks.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import app.suhocki.mybooks.data.api.GoogleDriveApi
import com.google.gson.Gson
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Provider

class GoogleDriveApiProvider @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val gson: Gson
): Provider<GoogleDriveApi> {

    override fun get(): GoogleDriveApi =
            Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BuildConfig.GOOGLE_DRIVE_API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(GoogleDriveApi::class.java)
}