package app.suhocki.mybooks.presentation.catalog

import android.support.design.widget.AppBarLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.widget.ImageView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.presentation.base.DividerItemDecoration
import app.suhocki.mybooks.presentation.base.themedToolbarCompat
import app.suhocki.mybooks.presentation.catalog.adapter.CatalogAdapter
import org.jetbrains.anko.*
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.themedAppBarLayout
import org.jetbrains.anko.recyclerview.v7.themedRecyclerView
import javax.inject.Inject


class CatalogUI @Inject constructor(
    private var adapter: CatalogAdapter,
    private var layoutManager: LinearLayoutManager
) : AnkoComponent<CatalogActivity> {
    lateinit var toolbar: Toolbar

    override fun createView(ui: AnkoContext<CatalogActivity>) = with(ui) {

        coordinatorLayout {
            fitsSystemWindows = true

            themedAppBarLayout(R.style.ThemeOverlay_AppCompat_Dark_ActionBar) {
                themedToolbarCompat(R.style.ThemeOverlay_AppCompat_Dark_ActionBar) {
                    toolbar = this
                    backgroundColorResource = R.color.colorPrimary
                    popupTheme = R.style.ThemeOverlay_AppCompat_Light

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
                id = R.id.id_recycler_catalog
                clipToPadding = false
                adapter = this@CatalogUI.adapter
                layoutManager = this@CatalogUI.layoutManager
                backgroundColorResource = R.color.gray
                addItemDecoration(DividerItemDecoration(dip(2)))
            }.lparams(matchParent, matchParent) {
                behavior = AppBarLayout.ScrollingViewBehavior()
            }
        }
    }
}