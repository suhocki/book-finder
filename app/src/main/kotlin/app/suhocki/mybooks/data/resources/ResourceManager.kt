package app.suhocki.mybooks.data.resources

import android.content.Context
import android.support.annotation.ArrayRes
import android.support.annotation.StringRes
import javax.inject.Inject

class ResourceManager @Inject constructor(private val context: Context) {

    fun getString(@StringRes id: Int) =
        context.getString(id)!!

    fun getStringArray(@ArrayRes id: Int) =
        context.resources.getStringArray(id)!!
}