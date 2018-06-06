package app.suhocki.mybooks.ui.search

import android.graphics.Color
import android.support.design.widget.AppBarLayout
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.attrResource
import app.suhocki.mybooks.ui.base.themedToolbarCompat
import app.suhocki.mybooks.ui.base.view.ScrollLayoutManager
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.tintedImageView
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.themedAppBarLayout
import org.jetbrains.anko.recyclerview.v7.themedRecyclerView
import org.jetbrains.anko.sdk25.coroutines.onClick

class SearchUI : AnkoComponent<SearchActivity> {

    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: View
    lateinit var emptyView: View

    override fun createView(ui: AnkoContext<SearchActivity>) = with(ui) {

        coordinatorLayout {
            fitsSystemWindows = false
            backgroundColorResource = R.color.colorWhite

            themedAppBarLayout(R.style.ThemeOverlay_AppCompat_Dark_ActionBar) {
                themedToolbarCompat(R.style.ThemeOverlay_AppCompat_Dark_ActionBar) {
                    owner.setSupportActionBar(this)
                    backgroundColorResource = R.color.colorPrimary
                    popupTheme = R.style.ThemeOverlay_AppCompat_Light

                    imageView(R.drawable.ic_close).apply {
                        onClick { owner.finish() }
                        padding = dimen(R.dimen.padding_toolbar_icon)
                        backgroundResource = context
                            .attrResource(R.attr.selectableItemBackgroundBorderless)
                        scaleType = ImageView.ScaleType.FIT_CENTER
                        layoutParams = Toolbar.LayoutParams(
                            dimenAttr(R.attr.actionBarSize),
                            matchParent
                        ).apply {
                            gravity = Gravity.END
                        }
                    }
                }.lparams(matchParent, matchParent) {
                    scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
                }
            }.lparams(matchParent, dimenAttr(R.attr.actionBarSize))

            themedRecyclerView(R.style.ScrollbarRecyclerView) {
                id = R.id.id_recycler_search
                isVerticalScrollBarEnabled = true
                clipToPadding = false
                recyclerView = this
                setHasFixedSize(true)
                layoutManager = ScrollLayoutManager(context)
            }.lparams(matchParent, matchParent) {
                behavior = AppBarLayout.ScrollingViewBehavior()
            }

            themedProgressBar(R.style.ColoredProgressBar) {
                progressBar = this
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

                    textView(R.string.empty_screen_authors) {
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
