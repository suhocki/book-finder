package app.suhocki.mybooks.ui.details

import android.graphics.Point
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.content.res.ResourcesCompat
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import app.suhocki.mybooks.*
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.ui.base.multilineCollapsingToolbarLayout
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.floatingActionButton
import org.jetbrains.anko.design.themedAppBarLayout
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.support.v4.nestedScrollView


class DetailsUI : AnkoComponent<DetailsActivity>, AnkoLogger {

    lateinit var book: Book
    private var windowHeight = 0

    override fun createView(ui: AnkoContext<DetailsActivity>) = with(ui) {
        calculateWindowHeight(owner)
        var coloredBackgrounds = 0

        coordinatorLayout {
            fitsSystemWindows = false

            themedAppBarLayout(R.style.ThemeOverlay_AppCompat_Dark_ActionBar) {
                id = R.id.id_app_bar_details
                fitsSystemWindows = false

                multilineCollapsingToolbarLayout {
                    maxLines = 2
                    fitsSystemWindows = false
                    setContentScrimResource(R.color.colorPrimary)
                    setExpandedTitleMargin(dip(16), dip(16), dip(32), dip(16))
                    setExpandedTitleTextAppearance(R.style.TextAppearance_AppCompat_Headline)
                    title = book.shortName

                    frameLayout {
                        foreground = ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.fade,
                            context.theme
                        )

                        imageView {
                            Picasso.get().load(book.productLink).into(this)
                            scaleType = ImageView.ScaleType.CENTER_CROP
                        }.lparams(matchParent, matchParent)
                    }.lparams(matchParent, matchParent) {
                        collapseMode = CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX
                    }

                    toolbar {
                        setContentInsetsRelative(dip(16), dip(16))
                        owner.setSupportActionBar(this)
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
                clipToPadding = false
                padding = dip(8)

                verticalLayout {
                    textView {
                        text = book.fullName
                        allCaps = true
                        textAppearance = R.style.TextAppearance_AppCompat_Headline
                    }

                    divider(dip(8), dip(8))

                    textView {
                        text = owner.getString(R.string.rubles, book.price)
                        textAppearance = R.style.TextAppearance_AppCompat_Headline
                    }.lparams {
                        setMargins(dip(0), dip(0), dip(0), dip(16))
                    }

                    linearLayout {
                        textView(R.string.isbn) { gravity = Gravity.CENTER_VERTICAL }
                            .lparams(0, matchParent) { weight = 0.5f }
                        textView { text = book.productCode }.lparams(0, matchParent) {
                            weight = 0.5f
                        }
                    }

                    linearLayout {
                        book.author ?: let { setGone(this) }
                        textView(R.string.author) { gravity = Gravity.CENTER_VERTICAL }
                            .lparams(0, matchParent) { weight = 0.5f }
                        textView { text = book.author }.lparams(0, matchParent) { weight = 0.5f }
                    }

                    linearLayout {
                        book.publisher ?: let { setGone(this) }
                        textView(R.string.publisher) { gravity = Gravity.CENTER_VERTICAL }
                            .lparams(0, matchParent) { weight = 0.5f }
                        textView { text = book.publisher }.lparams(0, matchParent) { weight = 0.5f }
                    }

                    linearLayout {
                        book.year ?: let { setGone(this) }
                        textView(R.string.year) { gravity = Gravity.CENTER_VERTICAL }
                            .lparams(0, matchParent) { weight = 0.5f }
                        textView { text = book.year }.lparams(0, matchParent) { weight = 0.5f }
                    }

                    linearLayout {
                        book.cover ?: let { setGone(this) }
                        textView(R.string.cover) { gravity = Gravity.CENTER_VERTICAL }
                            .lparams(0, matchParent) { weight = 0.5f }
                        textView { text = book.cover }.lparams(0, matchParent) { weight = 0.5f }
                    }

                    linearLayout {
                        book.format ?: let { setGone(this) }
                        textView(R.string.format) { gravity = Gravity.CENTER_VERTICAL }
                            .lparams(0, matchParent) { weight = 0.5f }
                        textView { text = book.format }.lparams(0, matchParent) { weight = 0.5f }
                    }

                    linearLayout {
                        book.pageCount ?: let { setGone(this) }
                        textView(R.string.pages) { gravity = Gravity.CENTER_VERTICAL }
                            .lparams(0, matchParent) { weight = 0.5f }
                        textView { text = book.pageCount }.lparams(0, matchParent) { weight = 0.5f }
                    }

                    linearLayout {
                        book.series ?: let { setGone(this) }
                        textView(R.string.series) { gravity = Gravity.CENTER_VERTICAL }
                            .lparams(0, matchParent) { weight = 0.5f }
                        textView { text = book.series }.lparams(0, matchParent) { weight = 0.5f }
                    }

                    linearLayout {
                        book.status ?: let { setGone(this) }
                        textView(R.string.status) { gravity = Gravity.CENTER_VERTICAL }
                            .lparams(0, matchParent) { weight = 0.5f }
                        textView { text = book.status }.lparams(0, matchParent) { weight = 0.5f }
                    }

                    divider(dip(16), dip(8)).apply {
                        if (book.description.isNullOrBlank()) setGone(this)
                    }

                    textView(R.string.description) {
                        if (book.description.isNullOrBlank()) setGone(this)
                        padding = dip(6)
                        gravity = Gravity.CENTER
                        backgroundColorResource = R.color.colorDarkGray
                        textAppearance = R.style.TextAppearance_AppCompat_Subhead
                    }

                    divider(dip(8), dip(16)).apply {
                        if (book.description.isNullOrBlank()) setGone(this)
                    }

                    textView {
                        if (book.description.isNullOrBlank()) setGone(this)
                        text = book.description
                    }
                }.applyRecursively { view ->
                    view.setPadding(dip(4), dip(6), dip(4), dip(6))
                    if (view.visibility == View.VISIBLE) {
                        when (view) {
                            is LinearLayout -> with(view) {
                                if (++coloredBackgrounds % 2 == 0) {
                                    view.setBackgroundResource(R.color.colorGray)
                                }
                            }
                        }
                    }
                }

            }.lparams(matchParent, matchParent) {
                behavior = AppBarLayout.ScrollingViewBehavior()
            }

            floatingActionButton {
                id = R.id.id_fab
                onClick {
                    Analytics.bookAddedToCart(book)
                    owner.openLink(book.website)
                }
                useCompatPadding = true
                imageResource = R.drawable.ic_buy
            }.lparams {
                anchorId = R.id.id_app_bar_details
                anchorGravity = Gravity.BOTTOM or Gravity.END
            }
        }
    }

    private fun @AnkoViewDslMarker _LinearLayout.divider(top: Int, bottom: Int) =
        view { backgroundResource = R.color.colorGray }.lparams(matchParent, dip(2)) {
            setMargins(dip(0), top, dip(0), bottom)
        }

    private fun calculateWindowHeight(owner: DetailsActivity) {
        val display = owner.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        windowHeight = size.y
    }
}