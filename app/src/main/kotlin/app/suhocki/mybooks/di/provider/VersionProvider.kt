package app.suhocki.mybooks.di.provider

import app.suhocki.mybooks.BuildConfig
import android.content.Context
import app.suhocki.mybooks.domain.model.Version
import javax.inject.Inject
import javax.inject.Provider

class VersionProvider @Inject constructor(
    private val context: Context
) : Provider<Version> {

    override fun get(): Version = VersionEntity(
        context.packageManager.getPackageInfo(
            context.packageName, 0
        ).versionName!!, BuildConfig.VERSION_CODE
    )

    private class VersionEntity(
        override val version: String,
        override val code: Int
    ) : Version
}