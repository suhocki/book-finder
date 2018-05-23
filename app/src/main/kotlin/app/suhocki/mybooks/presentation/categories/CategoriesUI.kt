package app.suhocki.mybooks.presentation.categories

import android.support.design.widget.AppBarLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.widget.ImageView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.presentation.base.themedToolbarCompat
import org.jetbrains.anko.*
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.themedAppBarLayout
import org.jetbrains.anko.recyclerview.v7.themedRecyclerView
import javax.inject.Inject


class CategoriesUI @Inject constructor() : AnkoComponent<CategoriesActivity> {
    lateinit var toolbar: Toolbar
    lateinit var recyclerView: RecyclerView

    override fun createView(ui: AnkoContext<CategoriesActivity>) = with(ui) {

        coordinatorLayout {
            fitsSystemWindows = true

            themedAppBarLayout(R.style.ThemeOverlay_AppCompat_Dark_ActionBar) {
                themedToolbarCompat(R.style.ThemeOverlay_AppCompat_Dark_ActionBar) {
                    toolbar = this
                    backgroundColorResource = R.color.colorPrimary
                    popupTheme = R.style.ThemeOverlay_AppCompat_Light

                    imageView(R.drawable.logo).apply {
                        scaleType = ImageView.ScaleType.FIT_CENTER
                        adjustViewBounds = true
                        layoutParams = Toolbar.LayoutParams(
                            Toolbar.LayoutParams.WRAP_CONTENT,
                            Toolbar.LayoutParams.MATCH_PARENT
                        ).apply {
                            margin = dip(8)
                            gravity = Gravity.CENTER_HORIZONTAL
                        }
                    }

                }.lparams(matchParent, matchParent) {
                    scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
                }
            }.lparams(matchParent, dimenAttr(R.attr.actionBarSize))

            themedRecyclerView(R.style.ScrollbarRecyclerView) {
                id = R.id.id_recycler_categories
                recyclerView = this
                clipToPadding = false
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            }.lparams(matchParent, matchParent) {
                behavior = AppBarLayout.ScrollingViewBehavior()
            }
        }
    }
}