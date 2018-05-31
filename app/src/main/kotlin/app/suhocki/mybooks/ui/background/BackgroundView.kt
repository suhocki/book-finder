package app.suhocki.mybooks.ui.background

import android.app.Notification
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(SkipStrategy::class)
interface BackgroundView : MvpView {

    fun showNotification(notification: Notification, isForeground: Boolean)

    fun stopForegroundMode()

    fun stopService()

    fun showCatalogScreen()
}