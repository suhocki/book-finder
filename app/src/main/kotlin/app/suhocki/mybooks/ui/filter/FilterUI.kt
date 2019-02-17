package app.suhocki.mybooks.ui.filter

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.attrResource
import app.suhocki.mybooks.hideKeyboard
import app.suhocki.mybooks.setForegroundCompat
import app.suhocki.mybooks.ui.Ids
import app.suhocki.mybooks.ui.base.decorator.DividerItemDecoration
import app.suhocki.mybooks.ui.base.themedToolbarCompat
import app.suhocki.mybooks.ui.base.view.ScrollLayoutManager
import org.jetbrains.anko.*
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.themedAppBarLayout
import org.jetbrains.anko.recyclerview.v7.recyclerView

class FilterUI : AnkoComponent<Context> {

    lateinit var recyclerView: RecyclerView
    lateinit var bottomPanel: ViewGroup
    lateinit var apply: View
    lateinit var reset: View
    lateinit var buttonsDivider: View

    override fun createView(ui: AnkoContext<Context>) = with(ui) {

        coordinatorLayout {
            fitsSystemWindows = false
            backgroundColorResource = R.color.colorGray

            themedAppBarLayout(R.style.ThemeOverlay_AppCompat_Dark_ActionBar) {
                themedToolbarCompat(R.style.ThemeOverlay_AppCompat_Dark_ActionBar) {
                    backgroundColorResource = R.color.colorPrimary
                    popupTheme = R.style.ThemeOverlay_AppCompat_Light
                    setTitle(R.string.filter)

                    imageView(R.drawable.ic_close).apply {
//                        onClick { (owner as OnFilterClickListener).onFilterCollapseClick() }
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

            recyclerView {
                id = Ids.recyclerFilter
                isVerticalScrollBarEnabled = true
                clipToPadding = false
                recyclerView = this
                layoutManager = ScrollLayoutManager(context)
                setOnTouchListener { _, _ -> hideKeyboard();false }
                addItemDecoration(
                    DividerItemDecoration(
                        context.dimen(R.dimen.height_divider_decorator),
                        divideOnlyHeaders = true,
                        divideTopAndBottom = true
                    )
                )
            }.lparams(matchParent, matchParent) {
                behavior = AppBarLayout.ScrollingViewBehavior()
            }

            frameLayout {
                bottomPanel = this
                backgroundColorResource = R.color.colorPrimary
                ViewCompat.setElevation(this, 50f)
                visibility = View.GONE

                textView(R.string.apply) {
                    apply = this
                    textAppearance = R.style.TextAppearance_AppCompat_Title_Inverse
                    horizontalPadding = dip(16)
                    gravity = Gravity.CENTER
                    setForegroundCompat(context.attrResource(R.attr.selectableItemBackgroundBorderless))
                }.lparams(dimen(R.dimen.navigation_drawer_width) / 2, matchParent) {
                    gravity = Gravity.START
                }

                textView(R.string.reset) {
                    reset = this
                    textAppearance = R.style.TextAppearance_AppCompat_Title_Inverse
                    horizontalPadding = dip(16)
                    gravity = Gravity.CENTER
                    setForegroundCompat(context.attrResource(R.attr.selectableItemBackgroundBorderless))
                }.lparams(dimen(R.dimen.navigation_drawer_width) / 2, matchParent) {
                    gravity = Gravity.END
                }

                view {
                    buttonsDivider = this
                    backgroundColorResource = R.color.colorWhite
                }.lparams(dip(2), matchParent) {
                    gravity = Gravity.CENTER_HORIZONTAL
                }
            }.lparams(matchParent, dimenAttr(R.attr.actionBarSize)) {
                gravity = Gravity.BOTTOM
            }
        }
    }
}