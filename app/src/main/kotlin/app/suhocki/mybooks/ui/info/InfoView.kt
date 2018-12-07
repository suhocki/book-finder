package app.suhocki.mybooks.ui.info

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface InfoView : MvpView {
    fun showInfoItems(items: MutableList<Any>)

    fun showAdminMode(enabled: Boolean)

    fun showDebugPanel(debugEnabled: Boolean)

    fun showProgress(isVisible: Boolean)

    fun showHiddenSettingsDialog(adminModeEnabled: Boolean, debugPanelEnabled: Boolean)
}