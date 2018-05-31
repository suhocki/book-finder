package app.suhocki.mybooks

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.DrawableRes
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.util.TypedValue
import android.view.View


fun Context.attrResource(@AttrRes attribute: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attribute, typedValue, true)
    return typedValue.resourceId
}

fun Context.isAppOnForeground(): Boolean {
    val activityManager = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
    val appProcesses = activityManager?.runningAppProcesses ?: return false
    val packageName = this.packageName
    for (appProcess in appProcesses) {
        if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND &&
            appProcess.processName == packageName) {
            return true
        }
    }
    return false
}

fun Context.isAppInBackground() = !isAppOnForeground()

fun Context.openLink(link: String) {
    CustomTabsIntent.Builder()
        .setToolbarColor(ResourcesCompat.getColor(resources, R.color.colorPrimary, theme))
        .build()
        .launchUrl(this, Uri.parse(link))
}

@Throws(InterruptedException::class)
fun checkThreadInterrupt() {
    if (Thread.currentThread().isInterrupted) throw InterruptedException()
}

fun setVisible(vararg views: View) {
    views.forEach { it.visibility = View.VISIBLE }
}

fun setGone(vararg views: View) {
    views.forEach { it.visibility = View.GONE }
}

fun View.setForegroundCompat(@DrawableRes drawableRes: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        foreground = ContextCompat.getDrawable(context, drawableRes)
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        background = ContextCompat.getDrawable(context, drawableRes)
    }
}

fun inDebug(action: () -> Unit) {
    if (BuildConfig.DEBUG) action.invoke()
}

fun inRelease(action: () -> Unit) {
    if (!BuildConfig.DEBUG) action.invoke()
}

fun Activity.inLandscape(action: () -> Unit) {
    if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
        action.invoke()
}

fun Context.color(colorRes: Int) = ContextCompat.getColor(this, colorRes)
