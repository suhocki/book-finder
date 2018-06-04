package app.suhocki.mybooks.ui.filter

import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.widget.ImageView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.attrResource
import app.suhocki.mybooks.ui.base.themedToolbarCompat
import app.suhocki.mybooks.ui.books.listener.DrawerHandler
import org.jetbrains.anko.*
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.themedAppBarLayout
import org.jetbrains.anko.recyclerview.v7.themedRecyclerView
import org.jetbrains.anko.sdk25.coroutines.onClick

class FilterUI<in T : Fragment> : AnkoComponent<T> {

    lateinit var recyclerView: RecyclerView

    override fun createView(ui: AnkoContext<T>) = with(ui) {

        coordinatorLayout {
            fitsSystemWindows = false
            backgroundColorResource = R.color.colorWhite

            themedAppBarLayout(R.style.ThemeOverlay_AppCompat_Dark_ActionBar) {
                themedToolbarCompat(R.style.ThemeOverlay_AppCompat_Dark_ActionBar) {
                    backgroundColorResource = R.color.colorPrimary
                    popupTheme = R.style.ThemeOverlay_AppCompat_Light
                    setTitle(R.string.filter)

                    imageView(R.drawable.ic_close).apply {
                        onClick { (owner.activity as DrawerHandler).setDrawerExpanded(false) }
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
                id = R.id.id_recycler_books
                isVerticalScrollBarEnabled = true
                clipToPadding = false
                recyclerView = this
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
            }.lparams(matchParent, matchParent) {
                behavior = AppBarLayout.ScrollingViewBehavior()
            }
        }
    }
}