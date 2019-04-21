package app.suhocki.mybooks.ui.books

import android.content.Context
import android.graphics.Color
import android.support.design.widget.AppBarLayout
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.graphics.drawable.DrawerArrowDrawable
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.View
import app.suhocki.mybooks.R
import app.suhocki.mybooks.ui.Ids
import app.suhocki.mybooks.ui.base.themedAutofitRecyclerView
import app.suhocki.mybooks.ui.base.themedToolbarCompat
import org.jetbrains.anko.*
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.themedAppBarLayout


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
                        .apply {
                            color = Color.WHITE
                        }

                    backgroundColorResource = R.color.colorPrimary
                    setContentInsetsRelative(0, 0)

                    textView(R.string.filter) {
                        gravity = Gravity.CENTER_VERTICAL
                        horizontalPadding = dip(16)
                        textColorResource = R.color.colorWhite
                        textSize = 16f
                        typeface = ResourcesCompat.getFont(context, R.font.roboto_medium)
                        layoutParams = Toolbar.LayoutParams(
                            Gravity.END or Gravity.CENTER_VERTICAL
                        )
                    }

                }.lparams(matchParent, matchParent) {
                    scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
                }
            }.lparams(matchParent, dimenAttr(R.attr.actionBarSize))

            themedAutofitRecyclerView(R.style.ScrollbarRecyclerView) {
                recyclerView = this
                id = Ids.recyclerCatalog
                clipToPadding = false
                setHasFixedSize(true)
                columnWidth = dip(146)
                backgroundColorResource = R.color.colorGray
                padding = resources.getDimensionPixelSize(R.dimen.height_divider_decorator)
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