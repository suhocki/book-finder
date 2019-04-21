package app.suhocki.mybooks.ui.details

import android.content.Context
import android.graphics.Point
import android.support.design.widget.AppBarLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import app.suhocki.mybooks.R
import app.suhocki.mybooks.attrResource
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.setGone
import app.suhocki.mybooks.toRoundedPrice
import app.suhocki.mybooks.ui.Ids
import app.suhocki.mybooks.ui.base.multilineCollapsingToolbarLayout
import app.suhocki.mybooks.ui.base.simpleDraweeView
import app.suhocki.mybooks.ui.base.view.MultilineCollapsingToolbarLayout
import com.facebook.drawee.view.SimpleDraweeView
import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.floatingActionButton
import org.jetbrains.anko.design.themedAppBarLayout
import org.jetbrains.anko.support.v4.nestedScrollView


class DetailsUI : AnkoComponent<Context> {
    lateinit var fabBuy: FloatingActionButton
    lateinit var image: SimpleDraweeView
    lateinit var toolbarLayout: MultilineCollapsingToolbarLayout
    lateinit var toolbar: Toolbar
    lateinit var scrollView: ViewGroup

    private var windowHeight = 0

    override fun createView(ui: AnkoContext<Context>) = with(ui) {
        calculateWindowHeight(owner)

        coordinatorLayout {
            fitsSystemWindows = false
            backgroundColorResource = R.color.colorWhite

            themedAppBarLayout(R.style.ThemeOverlay_AppCompat_Dark_ActionBar) {
                id = Ids.appbarDetails
                fitsSystemWindows = false

                multilineCollapsingToolbarLayout {
                    toolbarLayout = this@multilineCollapsingToolbarLayout
                    maxLines = 2
                    fitsSystemWindows = false
                    setContentScrimResource(R.color.colorPrimary)
                    setExpandedTitleMargin(dip(16), dip(16), dip(32), dip(16))
                    setExpandedTitleTextAppearance(R.style.TextAppearance_AppCompat_Headline)

                    frameLayout {
                        foreground = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.fade,
                            context.theme
                        )

                        simpleDraweeView {
                            this@DetailsUI.image = this
                            scaleType = ImageView.ScaleType.CENTER_CROP
                        }.lparams(matchParent, matchParent)
                    }.lparams(matchParent, matchParent) {
                        collapseMode = CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX
                    }

                    toolbar {
                        setContentInsetsRelative(dip(16), dip(16))
                        toolbar = this
                        popupTheme = R.style.ThemeOverlay_AppCompat_Light
                    }.lparams(
                        matchParent,
                        with(context) { dimen(attrResource(R.attr.actionBarSize)) }) {
                        collapseMode = CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN
                    }
                }.lparams(matchParent, matchParent) {
                    scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or
                            AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
                }

            }.lparams(matchParent, windowHeight / 2)

            nestedScrollView {
                scrollView = this
                clipToPadding = false
                padding = dip(8)


            }.lparams(matchParent, matchParent) {
                behavior = AppBarLayout.ScrollingViewBehavior()
            }

            floatingActionButton {
                fabBuy = this
                id = Ids.fab
                useCompatPadding = true
                imageResource = R.drawable.ic_buy
            }.lparams {
                anchorId = Ids.appbarDetails
                anchorGravity = Gravity.BOTTOM or Gravity.END
            }
        }
    }

    fun bindBook(book: Book) {
        var coloredBackgrounds = 0

        val context = scrollView.context
        val view = context.verticalLayout {
            textView {
                text = book.fullName
                allCaps = true
                textAppearance = R.style.TextAppearance_AppCompat_Headline
            }

            divider(dip(8), dip(8))

            textView {
                text = scrollView.resources.getString(R.string.rubles, book.price.toRoundedPrice())
                textAppearance = R.style.TextAppearance_AppCompat_Headline
            }.lparams {
                setMargins(dip(0), dip(0), dip(0), dip(16))
            }

            linearLayout {
                textView(R.string.isbn) { gravity = Gravity.CENTER_VERTICAL }
                    .lparams(0, matchParent) { weight = 0.5f }
                textView { text = book.id }.lparams(0, matchParent) {
                    weight = 0.5f
                }
            }

            linearLayout {
                book.author ?: setGone(this)
                textView(R.string.author) { gravity = Gravity.CENTER_VERTICAL }
                    .lparams(0, matchParent) { weight = 0.5f }
                textView { text = book.author }.lparams(0, matchParent) { weight = 0.5f }
            }

            linearLayout {
                book.publisher ?: setGone(this)
                textView(R.string.publisher) { gravity = Gravity.CENTER_VERTICAL }
                    .lparams(0, matchParent) { weight = 0.5f }
                textView { text = book.publisher }.lparams(0, matchParent) { weight = 0.5f }
            }

            linearLayout {
                book.year ?: setGone(this)
                textView(R.string.year) { gravity = Gravity.CENTER_VERTICAL }
                    .lparams(0, matchParent) { weight = 0.5f }
                textView { text = book.year }.lparams(0, matchParent) { weight = 0.5f }
            }

            linearLayout {
                book.cover ?: setGone(this)
                textView(R.string.cover) { gravity = Gravity.CENTER_VERTICAL }
                    .lparams(0, matchParent) { weight = 0.5f }
                textView { text = book.cover }.lparams(0, matchParent) { weight = 0.5f }
            }

            linearLayout {
                book.format ?: setGone(this)
                textView(R.string.format) { gravity = Gravity.CENTER_VERTICAL }
                    .lparams(0, matchParent) { weight = 0.5f }
                textView { text = book.format }.lparams(0, matchParent) { weight = 0.5f }
            }

            linearLayout {
                book.pageCount ?: setGone(this)
                textView(R.string.pages) { gravity = Gravity.CENTER_VERTICAL }
                    .lparams(0, matchParent) { weight = 0.5f }
                textView { text = book.pageCount }.lparams(0, matchParent) { weight = 0.5f }
            }

            linearLayout {
                book.series ?: setGone(this)
                textView(R.string.series) { gravity = Gravity.CENTER_VERTICAL }
                    .lparams(0, matchParent) { weight = 0.5f }
                textView { text = book.series }.lparams(0, matchParent) { weight = 0.5f }
            }

            linearLayout {
                book.status ?: setGone(this)
                textView(R.string.status) { gravity = Gravity.CENTER_VERTICAL }
                    .lparams(0, matchParent) { weight = 0.5f }
                textView { text = book.status }.lparams(0, matchParent) { weight = 0.5f }
            }

            divider(dip(16), dip(8)).apply {
                if (book.description.isNullOrBlank()) setGone(this)
            }

            textView {
                if (book.description.isNullOrBlank()) setGone(this)
                text = book.description
            }
        }.applyRecursively { view ->
            view.setPadding(context.dip(4), context.dip(6), context.dip(4), context.dip(6))
            if (view.visibility == View.VISIBLE) {
                when (view) {
                    is LinearLayout -> with(view) {
                        if (++coloredBackgrounds % 2 == 0) {
                            view.setBackgroundResource(app.suhocki.mybooks.R.color.colorGray)
                        }
                    }
                }
            }
        }

        scrollView.removeAllViews()
        scrollView.addView(view)
    }

    private fun @AnkoViewDslMarker _LinearLayout.divider(top: Int, bottom: Int) =
        view { backgroundResource = R.color.colorGray }.lparams(matchParent, dip(2)) {
            setMargins(dip(0), top, dip(0), bottom)
        }

    private fun calculateWindowHeight(owner: Context) {
        val display = owner.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        windowHeight = size.y
    }
}