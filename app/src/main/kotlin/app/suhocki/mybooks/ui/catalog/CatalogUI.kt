package app.suhocki.mybooks.ui.catalog

import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import app.suhocki.mybooks.R
import app.suhocki.mybooks.attrResource
import app.suhocki.mybooks.ui.base.DividerItemDecoration
import app.suhocki.mybooks.ui.base.DrawerHandler
import app.suhocki.mybooks.ui.base.themedToolbarCompat
import org.jetbrains.anko.*
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.themedAppBarLayout
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.recyclerview.v7.themedRecyclerView
import org.jetbrains.anko.sdk25.coroutines.onClick


class CatalogUI<in T : Fragment> : AnkoComponent<T> {
    lateinit var recyclerView: RecyclerView

    override fun createView(ui: AnkoContext<T>) = with(ui) {

        coordinatorLayout {
            fitsSystemWindows = false

            themedAppBarLayout(R.style.ThemeOverlay_AppCompat_Dark_ActionBar) {
                themedToolbarCompat(R.style.ThemeOverlay_AppCompat_Dark_ActionBar) {
                    backgroundColorResource = R.color.colorPrimary
                    setContentInsetsRelative(0,0)

                    imageView(R.drawable.ic_menu).apply {
                        backgroundResource = context
                            .attrResource(R.attr.selectableItemBackgroundBorderless)
                        padding = dimen(R.dimen.padding_toolbar_icon)
                        scaleType = ImageView.ScaleType.FIT_CENTER
                        layoutParams = Toolbar.LayoutParams(
                            dimenAttr(R.attr.actionBarSize),
                            matchParent
                        ).apply {
                            gravity = Gravity.START
                        }
                        onClick {
                            (owner.activity as DrawerHandler).setExpanded(true)
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

                    imageView(R.drawable.ic_search).apply {
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

                    view {
                        backgroundColorResource = R.color.colorGray
                        layoutParams = Toolbar.LayoutParams(
                            dip(1),
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
                recyclerView = this
                id = R.id.id_recycler_catalog
                clipToPadding = false
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
                backgroundColorResource = R.color.colorGray
                addItemDecoration(DividerItemDecoration(dip(2)))
            }.lparams(matchParent, matchParent) {
                behavior = AppBarLayout.ScrollingViewBehavior()
            }
        }
    }
}