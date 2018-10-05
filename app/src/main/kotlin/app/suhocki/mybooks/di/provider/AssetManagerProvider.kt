package app.suhocki.mybooks.di.provider

import android.content.res.AssetManager
import app.suhocki.mybooks.data.context.ContextManager
import javax.inject.Inject
import javax.inject.Provider

class AssetManagerProvider @Inject constructor(
    private val contextManager: ContextManager
) : Provider<AssetManager> {

    override fun get(): AssetManager =
        contextManager.currentContext.assets
}