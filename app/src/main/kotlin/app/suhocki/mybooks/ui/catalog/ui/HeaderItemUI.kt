package app.suhocki.mybooks.ui.catalog.ui

import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import app.suhocki.mybooks.R
import org.jetbrains.anko.*

class HeaderItemUI : AnkoComponent<ViewGroup> {
    lateinit var parent: View
    lateinit var title: TextView

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {

        frameLayout {
            this@HeaderItemUI.parent = this

            textView {
                this@HeaderItemUI.title = this
                allCaps = true
                textAppearance = R.style.TextAppearance_AppCompat_Subhead
                setTypeface(typeface, Typeface.BOLD)
                horizontalPadding = dip(18)
                gravity = Gravity.CENTER_VERTICAL
                minHeight = dimenAttr(R.attr.actionBarSize)
            }.lparams(wrapContent, wrapContent)
        }.apply {
            rootView.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }
}