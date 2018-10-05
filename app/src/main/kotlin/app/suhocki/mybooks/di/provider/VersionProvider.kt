package app.suhocki.mybooks.di.provider

import app.suhocki.mybooks.BuildConfig
import app.suhocki.mybooks.data.context.ContextManager
import app.suhocki.mybooks.domain.model.Version
import javax.inject.Inject
import javax.inject.Provider

class VersionProvider @Inject constructor(
    private val contextManager: ContextManager
) : Provider<Version> {

    override fun get(): Version = VersionEntity(
        contextManager.currentContext.packageManager.getPackageInfo(
            contextManager.currentContext.packageName, 0
        ).versionName!!, BuildConfig.VERSION_CODE
    )

    private class VersionEntity(
        override val version: String,
        override val code: Int
    ) : Version
}