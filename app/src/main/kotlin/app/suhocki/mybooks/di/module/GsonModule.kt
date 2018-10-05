package app.suhocki.mybooks.di.module

import android.content.res.AssetManager
import app.suhocki.mybooks.di.provider.AssetManagerProvider
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import toothpick.config.Module

class GsonModule : Module() {
    init {
        bind(Gson::class.java).toInstance(GsonBuilder().create())

        bind(AssetManager::class.java)
            .toProvider(AssetManagerProvider::class.java)
            .singletonInScope()
    }
}