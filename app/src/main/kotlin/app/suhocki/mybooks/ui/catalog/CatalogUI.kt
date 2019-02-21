package app.suhocki.mybooks.ui.catalog

import android.content.Context
import android.graphics.Color
import android.support.design.widget.AppBarLayout
import android.support.v7.graphics.drawable.DrawerArrowDrawable
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.hideKeyboard
import app.suhocki.mybooks.ui.Ids
import app.suhocki.mybooks.ui.base.themedToolbarCompat
import app.suhocki.mybooks.ui.base.view.ScrollLayoutManager
import org.jetbrains.anko.*
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.themedAppBarLayout
import org.jetbrains.anko.recyclerview.v7.themedRecyclerView


class CatalogUI : AnkoComponent<Context> {
    lateinit var recyclerView: RecyclerView
    lateinit var search: View
    lateinit var close: ImageView
    lateinit var toolbar: Toolbar
    lateinit var progressBar: View

    override fun createView(ui: AnkoContext<Context>) = with(ui) {

        coordinatorLayout {
            fitsSystemWindows = false

            themedAppBarLayout(R.style.ThemeOverlay_AppCompat_Dark_ActionBar) {
                themedToolbarCompat(R.style.ThemeOverlay_AppCompat_Dark_ActionBar) {
                    toolbar = this

                    navigationIcon = DrawerArrowDrawable(context!!)
                        .apply { color = Color.WHITE }

                    backgroundColorResource = R.color.colorPrimary
                    setContentInsetsRelative(0, 0)

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

            themedRecyclerView(R.style.ScrollbarRecyclerView) {
                recyclerView = this
                id = Ids.recyclerCatalog
                clipToPadding = false
                layoutManager = ScrollLayoutManager(context)
                backgroundColorResource = R.color.colorGray
                setOnTouchListener { _, _ -> hideKeyboard();false }
            }.lparams(matchParent, matchParent) {
                behavior = AppBarLayout.ScrollingViewBehavior()
            }

            themedProgressBar(R.style.AccentProgressBar) {
                progressBar = this
                visibility = View.GONE
                topPadding = dimenAttr(R.attr.actionBarSize)
            }.lparams {
                gravity = Gravity.CENTER
            }
        }
    }
}