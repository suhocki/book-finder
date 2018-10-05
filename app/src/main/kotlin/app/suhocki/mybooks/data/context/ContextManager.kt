package app.suhocki.mybooks.data.context

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import app.suhocki.mybooks.App
import javax.inject.Inject

class ContextManager @Inject constructor(
    private val app: App
) {

    private var _currentContext: Context? = null

    var currentContext: Context = app.applicationContext
        get() = _currentContext ?: app.applicationContext

    init {
        app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityPaused(p0: Activity?) {
                _currentContext = null
            }

            override fun onActivityResumed(p0: Activity?) {
                _currentContext = p0
            }

            override fun onActivityStarted(p0: Activity?) {
            }

            override fun onActivityDestroyed(p0: Activity?) {
            }

            override fun onActivitySaveInstanceState(p0: Activity?, p1: Bundle?) {
            }

            override fun onActivityStopped(p0: Activity?) {
            }

            override fun onActivityCreated(p0: Activity, p1: Bundle?) {
            }
        })
    }
}