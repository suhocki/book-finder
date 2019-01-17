package app.suhocki.mybooks.data.resources

import android.content.Context
import android.support.annotation.ArrayRes
import android.support.annotation.StringRes
import app.suhocki.mybooks.getStringArrayIdentifiers
import javax.inject.Inject

class ResourceManager @Inject constructor(
    private val context: Context
) {

    private val resources by lazy { context.resources }

    fun getString(@StringRes id: Int) =
        resources.getString(id)!!

    fun getString(@StringRes id: Int, double: Double) =
        resources.getString(id, double)!!

    fun getStringArray(@ArrayRes id: Int): Array<String> =
        resources.getStringArray(id)!!

    fun getStringArrayIdentifiers(@ArrayRes id: Int) =
        resources.getStringArrayIdentifiers(id)
}