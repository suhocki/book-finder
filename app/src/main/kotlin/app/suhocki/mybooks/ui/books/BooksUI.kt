package app.suhocki.mybooks.ui.books

import android.content.Context
import android.graphics.Color
import android.support.design.widget.AppBarLayout
import android.support.v7.graphics.drawable.DrawerArrowDrawable
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.View
import app.suhocki.mybooks.R
import app.suhocki.mybooks.ui.Ids
import app.suhocki.mybooks.ui.base.themedToolbarCompat
import app.suhocki.mybooks.ui.base.view.ScrollLayoutManager
import org.jetbrains.anko.*
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.themedAppBarLayout
import org.jetbrains.anko.recyclerview.v7.themedRecyclerView


class BooksUI : AnkoComponent<Context> {
    lateinit var recyclerView: RecyclerView
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