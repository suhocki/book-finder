package app.suhocki.mybooks.ui.base.adapter.ui.filter

import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.attrResource
import app.suhocki.mybooks.setForegroundCompat
import org.jetbrains.anko.*

class FilterCheckableItemUI : AnkoComponent<ViewGroup> {
    lateinit var parent: View
    lateinit var name: TextView
    lateinit var booksCount: TextView
    lateinit var checkBox: CheckBox

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {

        frameLayout {
            this@FilterCheckableItemUI.parent = this
            backgroundResource = R.color.colorWhite
            setForegroundCompat(context.attrResource(R.attr.selectableItemBackground))

            linearLayout {

                textView {
                    this@FilterCheckableItemUI.name = this
                    horizontalPadding = dip(16)
                    textAppearance = R.style.TextAppearance_AppCompat_Body1
                }.lparams(wrapContent, wrapContent) {
                    gravity = Gravity.CENTER_VERTICAL
                }

                textView {
                    booksCount = this
                }.lparams(wrapContent, wrapContent) {
                    gravity = Gravity.CENTER_VERTICAL
                }

            }.lparams(wrapContent, dimenAttr(R.attr.actionBarSize))

            frameLayout {

                checkBox {
                    this@FilterCheckableItemUI.checkBox = this
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