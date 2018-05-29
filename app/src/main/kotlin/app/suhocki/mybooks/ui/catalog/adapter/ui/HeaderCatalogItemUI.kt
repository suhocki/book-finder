package app.suhocki.mybooks.ui.catalog.adapter.ui

import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import app.suhocki.mybooks.R
import org.jetbrains.anko.*

class HeaderCatalogItemUI : AnkoComponent<ViewGroup> {
    lateinit var parent: View

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {

        frameLayout {
            this@HeaderCatalogItemUI.parent = this
            backgroundResource = R.color.colorPrimary

            textView(R.string.catalog) {
                allCaps = true
                textAppearance = R.style.TextAppearance_AppCompat_Subhead_Inverse
                setTypeface(typeface, Typeface.BOLD)
                leftPadding = dip(18)
                gravity = Gravity.CENTER_VERTICAL
            }.lparams(wrapContent, dimenAttr(R.attr.actionBarSize))
        }.apply {
            rootView.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }
}