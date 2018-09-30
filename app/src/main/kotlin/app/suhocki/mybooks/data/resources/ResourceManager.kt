package app.suhocki.mybooks.data.resources

import android.content.Context
import android.content.res.Resources
import android.support.annotation.ArrayRes
import android.support.annotation.StringRes
import app.suhocki.mybooks.R.string.name
import app.suhocki.mybooks.getStringArrayIdentifiers
import javax.inject.Inject

class ResourceManager @Inject constructor(private val context: Context) {

    private val resources = context.resources

    fun getString(@StringRes id: Int) =
        resources.getString(id)!!

    fun getString(@StringRes id: Int, double: Double) =
        resources.getString(id, double)!!

    fun getStringArray(@ArrayRes id: Int): Array<String> =
        resources.getStringArray(id)!!

    fun getIntArray(@ArrayRes id: Int) =
        resources.getIntArray(id)!!

    fun getStringIdentifier(name: String) =
            resources.getIdentifier(name, "string", context.packageName)

    fun getStringArrayIdentifiers(@ArrayRes id: Int) =
            resources.getStringArrayIdentifiers(id)
}