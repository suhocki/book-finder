package app.suhocki.mybooks.presentation.books

import android.graphics.Color
import android.support.design.widget.AppBarLayout
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.widget.ProgressBar
import app.suhocki.mybooks.R
import app.suhocki.mybooks.presentation.base.ItemDecoratorGrid
import app.suhocki.mybooks.presentation.base.themedToolbarCompat
import app.suhocki.mybooks.themedAutofitRecyclerView
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.tintedImageView
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.themedAppBarLayout


class BooksUI : AnkoComponent<BooksActivity> {

    lateinit var progressBar: ProgressBar
    lateinit var emptyView: View
    lateinit var recyclerView: RecyclerView

    override fun createView(ui: AnkoContext<BooksActivity>) = with(ui) {
        coordinatorLayout {
            fitsSystemWindows = true

            themedAppBarLayout(R.style.ThemeOverlay_AppCompat_Dark_ActionBar) {
                themedToolbarCompat(R.style.ThemeOverlay_AppCompat_Dark_ActionBar) {
                    owner.setSupportActionBar(this)
                    owner.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                    backgroundColorResource = R.color.colorPrimary
                    popupTheme = R.style.ThemeOverlay_AppCompat_Light
                }.lparams(matchParent, matchParent) {
                    scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
                }
            }.lparams(matchParent, dimenAttr(R.attr.actionBarSize))

            themedAutofitRecyclerView(R.style.ScrollbarRecyclerView) {
                id = R.id.id_recycler_books
                clipToPadding = false
                recyclerView = this
                setHasFixedSize(true)
                columnWidth = dip(146)
                addItemDecoration(ItemDecoratorGrid(dip(8)))
            }.lparams(matchParent, matchParent) {
                behavior = AppBarLayout.ScrollingViewBehavior()
            }

            themedProgressBar(R.style.ColoredProgressBar) {
                progressBar = this
                visibility = View.GONE
                topPadding = dimenAttr(R.attr.actionBarSize)
            }.lparams {
                gravity = Gravity.CENTER
            }

            verticalLayout {
                emptyView = this
                gravity = Gravity.CENTER
                visibility = View.GONE
                topPadding = dimenAttr(R.attr.actionBarSize)

                tintedImageView(R.drawable.ic_error).apply {
                    DrawableCompat.wrap(drawable).apply {
                        DrawableCompat.setTint(this, Color.GRAY)
                        setImageDrawable(this)
                    }

                    textView(R.string.empty_screen) {
                        gravity = Gravity.CENTER
                    }.lparams {
                        topMargin = dip(16)
                        bottomMargin = dip(16)
                    }
                }
            }.lparams {
                gravity = Gravity.CENTER
                rightMargin = dip(56)
                leftMargin = dip(56)
            }
        }
    }
}