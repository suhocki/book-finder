package app.suhocki.mybooks

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.DrawableRes
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import app.suhocki.mybooks.domain.model.License


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

fun Context.openMap(address: String) {
    val uriBuilder = Uri.Builder()
        .scheme("geo")
        .path("0,0")
        .appendQueryParameter("q", address)
    val intent = Intent(Intent.ACTION_VIEW, uriBuilder.build())
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    }
}

fun Context.openMap(latitude: Long, longitude: Long) {
    val i = Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?q=loc:$latitude,$longitude"))
    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    val mapsPackageName = "com.google.android.apps.maps"
    if (isPackageExisted(mapsPackageName)) {
        i.setClassName(mapsPackageName, "com.google.android.maps.MapsActivity")
        i.`package` = mapsPackageName
    }
    startActivity(i)
}


fun Context.isPackageExisted(targetPackage: String): Boolean {
    val pm = packageManager
    try {
        val info = pm.getPackageInfo(targetPackage, PackageManager.GET_META_DATA)
    } catch (e: PackageManager.NameNotFoundException) {
        return false
    }

    return true
}

fun Context.openCaller(number: String) {
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:+$number")
    startActivity(intent)
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

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showKeyboard() {
    val im = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    im.showSoftInput(this, InputMethodManager.SHOW_FORCED)
}

fun License.LicenseType.getHumanName(resources: Resources) = when (this) {
    License.LicenseType.MIT -> resources.getString(R.string.library_license_MIT)
    License.LicenseType.APACHE_2 -> resources.getString(R.string.library_license_APACHE_2)
    License.LicenseType.CUSTOM -> resources.getString(R.string.library_license_CUSTOM)
    License.LicenseType.NONE -> resources.getString(R.string.library_license_NONE)
}