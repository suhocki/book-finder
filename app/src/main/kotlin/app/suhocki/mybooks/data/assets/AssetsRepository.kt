package app.suhocki.mybooks.data.assets

import android.content.res.AssetManager
import app.suhocki.mybooks.BuildConfig
import app.suhocki.mybooks.domain.model.Changelog
import app.suhocki.mybooks.domain.model.License
import app.suhocki.mybooks.domain.repository.ChangelogRepository
import app.suhocki.mybooks.domain.repository.LicenseRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader
import javax.inject.Inject


class AssetsRepository @Inject constructor(
    private val assets: AssetManager,
    private val gson: Gson
) : LicenseRepository, ChangelogRepository {

    override fun getLicenses(): List<License> =
        fromAsset(BuildConfig.LICENSES_FILE_NAME)

    override fun getChangelog(): List<Changelog> =
        fromAsset(BuildConfig.CHANGELOG_FILE_NAME)


    private inline fun <reified T> fromAsset(pathToAsset: String) =
        assets.open(pathToAsset).use { stream ->
            gson.fromJson<T>(InputStreamReader(stream), object : TypeToken<T>() {}.type)
        }
}