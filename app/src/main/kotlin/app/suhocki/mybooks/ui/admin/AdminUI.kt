package app.suhocki.mybooks.ui.admin

import android.graphics.Color
import android.graphics.PorterDuff
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.graphics.drawable.DrawerArrowDrawable
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.attrResource
import app.suhocki.mybooks.hideKeyboard
import app.suhocki.mybooks.ui.Ids
import app.suhocki.mybooks.ui.base.decorator.DividerItemDecoration
import app.suhocki.mybooks.ui.base.themedToolbarCompat
import app.suhocki.mybooks.ui.base.view.ScrollLayoutManager
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.themedTintedImageView
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.themedAppBarLayout
import org.jetbrains.anko.recyclerview.v7.themedRecyclerView
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.appcompat.v7.tintedImageView


class AdminUI<in T : Fragment>(
    private val onRetryClick: () -> Unit
) : AnkoComponent<T> {
    lateinit var recyclerView: RecyclerView
    lateinit var toolbar: Toolbar
    lateinit var progressBar: ProgressBar
    lateinit var progressText: TextView
    lateinit var errorView: View
    lateinit var retry: View
    lateinit var errorText: TextView

    override fun createView(ui: AnkoContext<T>) = with(ui) {

        coordinatorLayout {
            fitsSystemWindows = false
            backgroundColorResource = R.color.colorGray

            themedAppBarLayout(R.style.ThemeOverlay_AppCompat_Dark_ActionBar) {
                themedToolbarCompat(R.style.ThemeOverlay_AppCompat_Dark_ActionBar) {
                    toolbar = this
                    backgroundColorResource = R.color.colorPrimary
                    setContentInsetsRelative(0, 0)
                    popupTheme = R.style.ThemeOverlay_AppCompat_Light
                    setTitle(R.string.control_panel)

                    navigationIcon = DrawerArrowDrawable(context!!)
                        .apply { color = Color.WHITE }

                    imageView(R.drawable.ic_refresh).apply {
                        retry = this
                        onClick { onRetryClick() }
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
                        layoutParams = Toolbar.LayoutParams(dip(1), matchParent)
                            .apply { gravity = Gravity.END }
                    }
                }.lparams(matchParent, matchParent) {
                    scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
                }
            }.lparams(matchParent, dimenAttr(R.attr.actionBarSize))

            themedRecyclerView(R.style.ScrollbarRecyclerView) {
                recyclerView = this
                id = Ids.recyclerInfo
                clipToPadding = false
                layoutManager = ScrollLayoutManager(context)
                addItemDecoration(
                    DividerItemDecoration(context.dimen(R.dimen.height_divider_decorator), 1)
                )
                setOnTouchListener { _, _ -> hideKeyboard();false }
            }.lparams(matchParent, matchParent) {
                behavior = AppBarLayout.ScrollingViewBehavior()
            }

            themedProgressBar(R.style.AccentProgressBar) {
                progressBar = this
                visibility = View.GONE
            }.lparams {
                gravity = Gravity.CENTER
            }

            textView {
                progressText = this
            }.lparams {
                gravity = Gravity.CENTER
            }

            verticalLayout {
                errorView = this
                visibility = View.GONE

                tintedImageView(R.drawable.ic_error) {
                    setColorFilter(
                        ContextCompat.getColor(context, R.color.colorDarkerGray),
                        PorterDuff.Mode.SRC_IN
                    )
                }.lparams {
                    bottomMargin = dip(8)
                    gravity = Gravity.CENTER_HORIZONTAL
                }

                textView {
                    errorText = this
                    gravity = Gravity.CENTER_HORIZONTAL
                }.lparams {
                    topMargin = dip(8)
                    gravity = Gravity.CENTER_HORIZONTAL
                }
            }.lparams {
                margin = dip(32)
                gravity = Gravity.CENTER
            }
        }
    }
}