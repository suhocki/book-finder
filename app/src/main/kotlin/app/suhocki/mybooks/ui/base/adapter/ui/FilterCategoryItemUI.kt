package app.suhocki.mybooks.ui.base.adapter.ui

import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import app.suhocki.mybooks.R
import org.jetbrains.anko.*

class FilterCategoryItemUI : AnkoComponent<ViewGroup> {
    lateinit var parent: View
    lateinit var title: TextView
    lateinit var imageState: ImageView
    lateinit var imageConfigurated: ImageView

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {

        frameLayout {
            this@FilterCategoryItemUI.parent = this
            backgroundResource = R.color.colorWhite

            linearLayout {

                textView {
                    this@FilterCategoryItemUI.title = this
                    horizontalPadding = dip(12)
                    textAppearance = R.style.TextAppearance_AppCompat_Body2
                }.lparams(wrapContent, wrapContent) {
                    gravity = Gravity.CENTER_VERTICAL
                }

                imageView(R.drawable.ic_dot) {
                    imageConfigurated = this
                }.lparams(dip(8), dip(8)) {
                    gravity = Gravity.CENTER_VERTICAL
                }

            }.lparams(wrapContent, dimenAttr(R.attr.actionBarSize))

            imageView {
                imageState = this
                padding = dip(14)
            }.lparams(
                dimenAttr(R.attr.actionBarSize),
                dimenAttr(R.attr.actionBarSize)
            ) {
                gravity = Gravity.END
            }
        }.apply {
            rootView.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }
}