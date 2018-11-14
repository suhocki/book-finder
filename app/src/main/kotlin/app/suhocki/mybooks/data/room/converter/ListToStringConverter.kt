package app.suhocki.mybooks.data.room.converter

import android.arch.persistence.room.TypeConverter
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.di.module.GsonModule
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import toothpick.Toothpick

class ListToStringConverter {

    private val scope by lazy {
        Toothpick.openScopes(DI.APP_SCOPE, DI.GSON_SCOPE)
            .apply { installModules(GsonModule()) }
    }

    private val gson by lazy {
        scope.getInstance(Gson::class.java)
    }

    @TypeConverter
    fun fromList(list: List<String>): String =
        gson.toJson(list)

    @TypeConverter
    fun fromString(string: String): List<String> =
        gson.fromJson<List<String>>(string, object : TypeToken<List<String>>() {}.type)
}