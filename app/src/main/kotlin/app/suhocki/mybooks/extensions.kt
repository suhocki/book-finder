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
import android.os.Handler
import android.os.Looper
import android.support.annotation.ArrayRes
import android.support.annotation.AttrRes
import android.support.annotation.DrawableRes
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import app.suhocki.mybooks.domain.model.License
import retrofit2.HttpException
import retrofit2.Response
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.terrakok.cicerone.commands.BackTo
import ru.terrakok.cicerone.commands.Replace
import java.text.SimpleDateFormat
import java.util.*


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
            appProcess.processName == packageName
        ) {
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
    val i = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("http://maps.google.com/maps?q=loc:$latitude,$longitude")
    )
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
        @Suppress("UNUSED_VARIABLE")
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

@Throws(HttpException::class)
fun <T> Response<T>.getResponse(): T =
    if (isSuccessful) body()!!
    else throw HttpException(this)


fun Resources.getStringArrayIdentifiers(@ArrayRes arrayRes: Int): IntArray {
    val typedArray = obtainTypedArray(arrayRes)
    val idsArray = IntArray(typedArray.length())
    repeat(typedArray.length()) {
        idsArray[it] = typedArray.getResourceId(it, 0)
    }
    typedArray.recycle()
    return idsArray
}

fun String.toHumanDate(locale: Locale): String {
    val simpleDateFormat = SimpleDateFormat("d MMM", locale)
    val currentDateString = simpleDateFormat.format(Date())
    simpleDateFormat.timeZone = TimeZone.getTimeZone("GMT+0")
    simpleDateFormat.applyPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val shultzTime = simpleDateFormat.parse(this)
    simpleDateFormat.applyPattern("d MMM")
    simpleDateFormat.timeZone = TimeZone.getDefault()
    val shultzDateString = simpleDateFormat.format(shultzTime)
    simpleDateFormat.applyPattern("HH:mm")
    return if (shultzDateString == currentDateString) simpleDateFormat.format(shultzTime)
    else shultzDateString.replace(".", "")
}

fun Long.toHumanFileSize() = when (Math.round(toString().length.toFloat() / 3)) {
    1 -> "$this B"

    2 -> "${Math.round(this / 1000f)} KB"

    3 -> "${Math.round(this / 1000_000f)} MB"

    4 -> "${Math.round(this / 1000_000_000f)} GB"

    else -> "$this bytes"
}

fun Double.toRoundedPrice() =
    (Math.round(this * 100) / 100.0).toString()

fun uiThread(action: () -> Unit) {
    Handler(Looper.getMainLooper()).post(action)
}

fun <T> MutableList<T>.replaceInRange(data: List<T>, offset: Int, limit: Int) {
    if (data.isEmpty()) {
        addAll(data)
        return
    }

    val from = if (offset <= lastIndex) offset else size
    val to = if (offset + limit <= lastIndex) offset + limit else size

    subList(from, to).clear()
    addAll(from, data)
}

fun Any.objectScopeName() = "${javaClass.simpleName}_${hashCode()}"

fun Navigator.setLaunchScreen(screen: SupportAppScreen) {
    applyCommands(
        arrayOf(
            BackTo(null),
            Replace(screen)
        )
    )
}

fun Activity.hideKeyboard() {
    currentFocus?.apply {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}