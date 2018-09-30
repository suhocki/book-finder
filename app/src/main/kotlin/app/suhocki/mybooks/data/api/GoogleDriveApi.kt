package app.suhocki.mybooks.data.api

import app.suhocki.mybooks.data.api.entity.MetaData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Streaming
import retrofit2.http.Url

interface GoogleDriveApi {

    @GET("files")
    fun getFolderContents(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int
    ): Call<MetaData>

    @Streaming
    @GET
    fun downloadFile(@Url fileUrl: String): Call<ResponseBody>
}