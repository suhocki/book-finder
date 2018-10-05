package app.suhocki.mybooks.data.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Streaming
import retrofit2.http.Url

interface FilesApi {

    @Streaming
    @GET("uc")
    fun downloadFile(
        @Query("id") id: String,
        @Query("export") export: String
    ): Call<ResponseBody>
}