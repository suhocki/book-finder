package app.suhocki.mybooks.data.api

import app.suhocki.mybooks.data.api.entity.MetaData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleDriveApi {

    @GET("files")
    fun getFolderContents(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int,
        @Query("orderBy") orderBy: String
    ): Call<MetaData>
}