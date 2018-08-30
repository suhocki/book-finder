package app.suhocki.mybooks.ui.changelog

import android.support.design.widget.AppBarLayout
import android.support.v7.widget.RecyclerView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.ui.Ids
import app.suhocki.mybooks.ui.base.decorator.DividerItemDecoration
import app.suhocki.mybooks.ui.base.themedToolbarCompat
import app.suhocki.mybooks.ui.base.view.ScrollLayoutManager
import org.jetbrains.anko.*
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.themedAppBarLayout
import org.jetbrains.anko.recyclerview.v7.themedRecyclerView


class ChangelogUI : AnkoComponent<ChangelogActivity> {

    lateinit var recyclerView: RecyclerView

    override fun createView(ui: AnkoContext<ChangelogActivity>) = with(ui) {

        coordinatorLayout {
            fitsSystemWindows = false
            backgroundColorResource = R.color.colorGray

            themedAppBarLayout(R.style.ThemeOverlay_AppCompat_Dark_ActionBar) {
                themedToolbarCompat(R.style.ThemeOverlay_AppCompat_Dark_ActionBar) {
                    owner.setSupportActionBar(this)
                    owner.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                    backgroundColorResource = R.color.colorPrimary
                    popupTheme = R.style.ThemeOverlay_AppCompat_Light
                    setTitle(R.string.changelog)
                    owner.setSupportActionBar(this)
                }.lparams(matchParent, matchParent) {
                    scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
                }
            }.lparams(matchParent, dimenAttr(R.attr.actionBarSize))

            themedRecyclerView(R.style.ScrollbarRecyclerView) {
                id = Ids.recyclerChangelog
                isVerticalScrollBarEnabled = true
                recyclerView = this
                setHasFixedSize(true)
                layoutManager = ScrollLayoutManager(context)
                addItemDecoration(
                    DividerItemDecoration(
                        context.dimen(R.dimen.height_divider_decorator),
                        1
                    )
                )
            }.lparams(matchParent, matchParent) {
                behavior = AppBarLayout.ScrollingViewBehavior()
            }
        }
    }
}