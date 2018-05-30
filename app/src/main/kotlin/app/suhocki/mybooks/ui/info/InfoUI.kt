package app.suhocki.mybooks.ui.info

import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.widget.ImageView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.ui.base.listener.NavigationHandler
import app.suhocki.mybooks.ui.base.themedToolbarCompat
import org.jetbrains.anko.*
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.themedAppBarLayout
import org.jetbrains.anko.sdk25.coroutines.onClick

class InfoUI<in T : Fragment> : AnkoComponent<T> {

    override fun createView(ui: AnkoContext<T>) = with(ui) {

        coordinatorLayout {
            fitsSystemWindows = false

            themedAppBarLayout(R.style.ThemeOverlay_AppCompat_Dark_ActionBar) {
                themedToolbarCompat(R.style.ThemeOverlay_AppCompat_Dark_ActionBar) {
                    backgroundColorResource = R.color.colorPrimary
                    setContentInsetsRelative(0,0)
                    popupTheme = R.style.ThemeOverlay_AppCompat_Light

                    imageView(R.drawable.ic_menu).apply {
                        padding = dimen(R.dimen.padding_toolbar_icon)
                        scaleType = ImageView.ScaleType.FIT_CENTER
                        layoutParams = Toolbar.LayoutParams(
                            dimenAttr(R.attr.actionBarSize),
                            matchParent
                        ).apply {
                            gravity = Gravity.START
                        }
                        onClick {
                            (owner.activity as NavigationHandler).setDrawerExpanded(true)
                        }
                    }

                    imageView(R.drawable.logo).apply {
                        scaleType = ImageView.ScaleType.FIT_CENTER
                        layoutParams = Toolbar.LayoutParams(
                            Toolbar.LayoutParams.WRAP_CONTENT,
                            Toolbar.LayoutParams.WRAP_CONTENT
                        ).apply {
                            margin = dip(8)
                            gravity = Gravity.CENTER_HORIZONTAL
                        }
                    }
                }.lparams(matchParent, matchParent) {
                    scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
                }
            }.lparams(matchParent, dimenAttr(R.attr.actionBarSize))


        }
    }
}