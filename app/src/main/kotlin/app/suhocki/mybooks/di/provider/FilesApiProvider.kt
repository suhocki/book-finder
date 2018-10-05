package app.suhocki.mybooks.di.provider

import app.suhocki.mybooks.BuildConfig
import app.suhocki.mybooks.data.api.FilesApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import app.suhocki.mybooks.data.api.GoogleDriveApi
import com.google.gson.Gson
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Provider

class FilesApiProvider @Inject constructor(
    private val okHttpClient: OkHttpClient
): Provider<FilesApi> {

    override fun get(): FilesApi =
            Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BuildConfig.GOOGLE_DRIVE_FILE_API_URL)
                .build()
                .create(FilesApi::class.java)
}