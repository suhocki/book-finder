package app.suhocki.mybooks.ui.base.eventbus

import android.support.annotation.StringRes

data class ErrorEvent(
    @StringRes val messageRes: Int
)