package app.suhocki.mybooks.data.resources

import android.support.annotation.ArrayRes
import android.support.annotation.StringRes
import app.suhocki.mybooks.data.context.ContextManager
import app.suhocki.mybooks.getStringArrayIdentifiers
import javax.inject.Inject

class ResourceManager @Inject constructor(
    private val contextManager: ContextManager
) {

    private val resources by lazy { contextManager.currentContext.resources }

    fun getString(@StringRes id: Int) =
        resources.getString(id)!!

    fun getString(@StringRes id: Int, double: Double) =
        resources.getString(id, double)!!

    fun getStringArray(@ArrayRes id: Int): Array<String> =
        resources.getStringArray(id)!!

    fun getStringArrayIdentifiers(@ArrayRes id: Int) =
        resources.getStringArrayIdentifiers(id)
}