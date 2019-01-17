package app.suhocki.mybooks.di.provider

import android.content.res.AssetManager
import android.content.Context
import javax.inject.Inject
import javax.inject.Provider

class AssetManagerProvider @Inject constructor(
    private val context: Context
) : Provider<AssetManager> {

    override fun get(): AssetManager =
        context.assets
}