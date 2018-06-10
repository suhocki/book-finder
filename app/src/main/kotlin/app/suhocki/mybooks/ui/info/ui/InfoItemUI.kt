package app.suhocki.mybooks.ui.info.ui

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.attrResource
import app.suhocki.mybooks.setForegroundCompat
import org.jetbrains.anko.*

class InfoItemUI : AnkoComponent<ViewGroup> {
    lateinit var parent: View
    lateinit var name: TextView
    lateinit var icon: ImageView

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {

        frameLayout {
            this@InfoItemUI.parent = this
            setForegroundCompat(context.attrResource(R.attr.selectableItemBackground))
            backgroundColor = Color.WHITE

            linearLayout {

                imageView {
                    icon = this
                }.lparams(dip(20), matchParent)

                textView {
                    name = this
                    gravity = Gravity.CENTER_VERTICAL
                    textAppearance = R.style.TextAppearance_AppCompat_Body2
                }.lparams(matchParent, matchParent) {
                    leftMargin = dip(16)
                }
            }.lparams(matchParent, dimenAttr(R.attr.actionBarSize)) {
                leftMargin = dip(16)
                rightMargin = dip(16)
            }
        }.apply {
            rootView.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }
}