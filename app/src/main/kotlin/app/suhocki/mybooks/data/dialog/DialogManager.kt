package app.suhocki.mybooks.data.dialog

import android.content.Context
import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import android.view.WindowManager
import app.suhocki.mybooks.R
import javax.inject.Inject

class DialogManager @Inject constructor(
    private val context: Context
) {
    fun showErrorDialog(context: Context, @StringRes messageRes: Int, onRetry: () -> Unit) {
        AlertDialog.Builder(context, R.style.Theme_AppCompat_Light_Dialog_Alert)
            .setMessage(messageRes)
            .setPositiveButton(R.string.retry) { _, _ -> onRetry() }
            .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
            .setCancelable(false)
            .show()
    }
}