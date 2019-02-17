package app.suhocki.mybooks.ui.base.ui

import android.view.Gravity
import android.view.View
import android.view.ViewManager
import android.widget.CheckBox
import app.suhocki.mybooks.R
import app.suhocki.mybooks.ui.base.materialButton
import org.jetbrains.anko.*

class AppSettingsUi {
    lateinit var parent: View
    lateinit var apply: View
    lateinit var adminMode: CheckBox
    lateinit var debugPanel: CheckBox

    fun createView(ui: ViewManager) = with(ui) {

        verticalLayout {
            horizontalPadding = dip(12)
            topPadding = dip(16)

            textView(R.string.debug_menu) {
                leftPadding = dip(12)
                textAppearance = R.style.TextAppearance_AppCompat_Title
                bottomPadding = dip(18)
            }

            checkBox(R.string.admin_mode) {
                adminMode = this
                textAppearance = R.style.TextAppearance_AppCompat_Body2
            }.lparams {
                bottomMargin = dip(6)
            }

            checkBox(R.string.debug_panel) {
                debugPanel = this
                textAppearance = R.style.TextAppearance_AppCompat_Body2
            }

            materialButton {
                apply = this
                textResource = R.string.apply
                gravity = Gravity.CENTER
            }.lparams(wrapContent, wrapContent) {
                gravity = Gravity.END
                margin = dip(8)
            }
        }
    }
}