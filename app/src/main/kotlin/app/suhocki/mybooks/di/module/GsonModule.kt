package app.suhocki.mybooks.di.module

import android.content.Context
import android.content.res.AssetManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import toothpick.config.Module

class GsonModule(context: Context) : Module() {
    init {
        bind(Gson::class.java).toInstance(GsonBuilder().create())

        bind(AssetManager::class.java).toInstance(context.assets)
    }
}