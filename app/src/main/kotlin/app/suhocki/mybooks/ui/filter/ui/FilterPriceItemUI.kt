package app.suhocki.mybooks.ui.filter.ui

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import app.suhocki.mybooks.R
import org.jetbrains.anko.*

class FilterPriceItemUI : AnkoComponent<ViewGroup> {
    lateinit var parent: View

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {

        linearLayout {
            this@FilterPriceItemUI.parent = this
            backgroundResource = R.color.colorWhite

            editText {

            }.lparams(0, matchParent) {
                weight = 0.5f
            }

            editText {

            }.lparams(0, matchParent) {
                weight = 0.5f
            }
        }.apply {
            rootView.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dimenAttr(R.attr.actionBarSize)
            )
        }
    }
}