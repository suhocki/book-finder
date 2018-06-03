package app.suhocki.mybooks.ui.books

import android.graphics.Color
import android.support.design.widget.AppBarLayout
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import app.suhocki.mybooks.R
import app.suhocki.mybooks.attrResource
import app.suhocki.mybooks.ui.base.ScrollAwareFABBehavior
import app.suhocki.mybooks.ui.base.adapter.decorator.ItemDecoratorGrid
import app.suhocki.mybooks.ui.base.themedAutofitRecyclerView
import app.suhocki.mybooks.ui.base.themedToolbarCompat
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.tintedImageView
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.floatingActionButton
import org.jetbrains.anko.design.themedAppBarLayout
import org.jetbrains.anko.sdk25.coroutines.onClick


class BooksUI : AnkoComponent<BooksActivity> {

    lateinit var progressBar: ProgressBar
    lateinit var emptyView: View
    lateinit var recyclerView: RecyclerView
    lateinit var fabFilter: View
    lateinit var menuFilter: View

    override fun createView(ui: AnkoContext<BooksActivity>) = with(ui) {
        coordinatorLayout {
            fitsSystemWindows = false

            themedAppBarLayout(R.style.ThemeOverlay_AppCompat_Dark_ActionBar) {
                themedToolbarCompat(R.style.ThemeOverlay_AppCompat_Dark_ActionBar) {
                    owner.setSupportActionBar(this)
                    owner.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                    backgroundColorResource = R.color.colorPrimary
                    popupTheme = R.style.ThemeOverlay_AppCompat_Light

                    imageView(R.drawable.ic_horizontal_menu).apply {
                        menuFilter = this
                        onClick {  }
                        padding = dimen(R.dimen.padding_toolbar_icon) + dip(2)
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

            themedAutofitRecyclerView {
                id = R.id.id_recycler_books
                isVerticalScrollBarEnabled = true
                clipToPadding = false
                recyclerView = this
                setHasFixedSize(true)
                columnWidth = dip(146)
                addItemDecoration(ItemDecoratorGrid(dip(4)))
                padding = dip(4)
                fastScrollMargin = dip(8)
                fastScrollThickness = dip(10)
                fastScrollMinimumHeight = dip(48)
                enableFastScroller(
                    R.drawable.thumb_drawable,
                    R.drawable.line_drawable,
                    R.drawable.thumb_drawable,
                    R.drawable.line_drawable
                )
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

            floatingActionButton {
                fabFilter = this
                useCompatPadding = true
                onClick {  }
                imageResource = R.drawable.ic_filter
            }.lparams {
                behavior = ScrollAwareFABBehavior()
                gravity = Gravity.BOTTOM or Gravity.END
            }
        }
    }
}