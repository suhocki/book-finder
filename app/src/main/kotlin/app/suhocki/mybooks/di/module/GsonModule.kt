package app.suhocki.mybooks.di.module

import android.content.Context
import android.content.res.AssetManager
import app.suhocki.mybooks.data.repository.AssetsRepository
import app.suhocki.mybooks.domain.repository.LicenseRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import toothpick.config.Module

class GsonModule(context: Context) : Module() {
    init {
        bind(Gson::class.java).toInstance(GsonBuilder().create())

        bind(AssetManager::class.java).toInstance(context.assets)
    }
}