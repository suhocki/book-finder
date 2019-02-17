package app.suhocki.mybooks.ui.base.ui

import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import app.suhocki.mybooks.R
import org.jetbrains.anko.*

class FilterSubCategoryItemUI : AnkoComponent<ViewGroup> {
    lateinit var parent: View
    lateinit var name: TextView
    lateinit var booksCount: TextView
    lateinit var checkBox: CheckBox

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        frameLayout {
            this@FilterSubCategoryItemUI.parent = this
            backgroundResource = R.color.colorWhite

            linearLayout {
                rightPadding = dimenAttr(R.attr.actionBarSize)

                textView {
                    this@FilterSubCategoryItemUI.name = this
                    maxLines = 1
                    ellipsize = TextUtils.TruncateAt.END
                    leftPadding = dip(16)
                    textAppearance = R.style.TextAppearance_AppCompat_Body1
                }.lparams(wrapContent, wrapContent) {
                    weight = 1f
                    gravity = Gravity.CENTER_VERTICAL
                }

                textView {
                    booksCount = this
                }.lparams(wrapContent, wrapContent) {
                    gravity = Gravity.CENTER_VERTICAL
                    leftMargin = dip(16)
                }

            }.lparams(wrapContent, dimenAttr(R.attr.actionBarSize))

            frameLayout {

                checkBox {
                    this@FilterSubCategoryItemUI.checkBox = this
                }.lparams {
                    gravity = Gravity.CENTER
                }

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