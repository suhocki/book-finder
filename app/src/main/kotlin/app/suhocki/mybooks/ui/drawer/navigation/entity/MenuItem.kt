package app.suhocki.mybooks.ui.drawer.navigation.entity

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes

data class MenuItem constructor(
    val id: Int,
    @StringRes val nameRes: Int,
    @DrawableRes val iconRes: Int,
    var isSelected: Boolean = false
)