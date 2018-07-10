package app.suhocki.mybooks.data.repository

import android.content.res.AssetManager
import app.suhocki.mybooks.domain.model.License
import app.suhocki.mybooks.domain.repository.LicenseRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader
import javax.inject.Inject


class AssetsRepository @Inject constructor(
    private val assets: AssetManager,
    private val gson: Gson
) : LicenseRepository {

    override fun getLicenses(): List<License> =
        fromAsset(LICENSES_FILE_NAME)

    private inline fun <reified T> fromAsset(pathToAsset: String) =
        assets.open(pathToAsset).use { stream ->
            gson.fromJson<T>(InputStreamReader(stream), object : TypeToken<T>() {}.type)
        }


    companion object {
        private const val LICENSES_FILE_NAME = "app/app_libraries.json"
    }
}