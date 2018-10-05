package app.suhocki.mybooks.data.dialog

import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.context.ContextManager
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
}