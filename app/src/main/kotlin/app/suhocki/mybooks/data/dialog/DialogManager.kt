package app.suhocki.mybooks.data.dialog

import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import android.view.Gravity
import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.context.ContextManager
import app.suhocki.mybooks.ui.base.materialButton
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import javax.inject.Inject

class DialogManager @Inject constructor(
    private val contextManager: ContextManager
) {
    fun showErrorDialog(@StringRes messageRes: Int, onRetry: () -> Unit) {
        AlertDialog.Builder(
            contextManager.currentContext,
            R.style.Theme_MaterialComponents_Light_Dialog_Alert
        ).setMessage(messageRes)
            .setPositiveButton(R.string.retry) { _, _ -> onRetry() }
            .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
            .setCancelable(false)
            .show()
    }

    fun showHiddenSettingsDialog(
        adminModeEnabled: Boolean,
        debugPanelEnabled: Boolean,
        resultListener: (Boolean, Boolean) -> Unit
    ) {
        contextManager.currentContext
            .alert {
                customView {
                    verticalLayout {
                        horizontalPadding = dip(12)
                        topPadding = dip(16)

                        textView(R.string.debug_menu) {
                            leftPadding = dip(12)
                            textAppearance = R.style.TextAppearance_AppCompat_Title
                            bottomPadding = dip(18)
                        }

                        val adminMode = checkBox(R.string.admin_mode) {
                            isChecked = adminModeEnabled
                            textAppearance = R.style.TextAppearance_AppCompat_Body2
                        }.lparams {
                            bottomMargin = dip(6)
                        }

                        val debugPanel = checkBox(R.string.debug_panel) {
                            isChecked = debugPanelEnabled
                            textAppearance = R.style.TextAppearance_AppCompat_Body2
                        }

                        materialButton {
                            textResource = R.string.apply
                            gravity = Gravity.CENTER

                            onClick {
                                resultListener.invoke(adminMode.isChecked, debugPanel.isChecked)
                            }
                        }.lparams(wrapContent, wrapContent) {
                            gravity = Gravity.END
                            margin = dip(8)
                        }
                    }
                }
            }
            .show()
    }
}