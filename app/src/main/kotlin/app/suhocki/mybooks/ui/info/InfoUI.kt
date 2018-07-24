package app.suhocki.mybooks.ui.info

import android.graphics.Color
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v7.graphics.drawable.DrawerArrowDrawable
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import app.suhocki.mybooks.R
import app.suhocki.mybooks.hideKeyboard
import app.suhocki.mybooks.ui.base.decorator.DividerItemDecoration
import app.suhocki.mybooks.ui.base.themedToolbarCompat
import app.suhocki.mybooks.ui.base.view.ScrollLayoutManager
import org.jetbrains.anko.*
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.themedAppBarLayout
import org.jetbrains.anko.recyclerview.v7.themedRecyclerView

class InfoUI<in T : Fragment> : AnkoComponent<T> {

    lateinit var recyclerView: RecyclerView
    lateinit var toolbar: Toolbar

    override fun createView(ui: AnkoContext<T>) = with(ui) {

        coordinatorLayout {
            fitsSystemWindows = false
            backgroundColorResource = R.color.colorGray

            themedAppBarLayout(R.style.ThemeOverlay_AppCompat_Dark_ActionBar) {
                themedToolbarCompat(R.style.ThemeOverlay_AppCompat_Dark_ActionBar) {
                    toolbar = this
                    backgroundColorResource = R.color.colorPrimary
                    setContentInsetsRelative(0, 0)
                    popupTheme = R.style.ThemeOverlay_AppCompat_Light
                    setTitle(R.string.information)

                    navigationIcon = DrawerArrowDrawable(context!!)
                        .apply { color = Color.WHITE }

                }.lparams(matchParent, matchParent) {
                    scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
                }
            }.lparams(matchParent, dimenAttr(R.attr.actionBarSize))

            themedRecyclerView(R.style.ScrollbarRecyclerView) {
                recyclerView = this
                id = R.id.id_recycler_catalog
                clipToPadding = false
                layoutManager = ScrollLayoutManager(context)
                addItemDecoration(
                    DividerItemDecoration(
                        context.dimen(R.dimen.height_divider_decorator),
                        divideOnlyHeaders = true
                    )
                )
                setOnTouchListener { _, _ -> hideKeyboard();false }
            }.lparams(matchParent, matchParent) {
                behavior = AppBarLayout.ScrollingViewBehavior()
            }
        }
    }
}