package app.suhocki.mybooks.ui.filter.ui

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

class EmptyCategoryItemUI : AnkoComponent<ViewGroup> {
    lateinit var parent: View
    lateinit var name: TextView

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {

        frameLayout {
            this@EmptyCategoryItemUI.parent = this
            backgroundResource = R.color.colorWhite

            textView {
                name = this
            }.lparams {
                marginStart = dip(16)
                verticalMargin = dip(8)
            }
        }.apply {
            rootView.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }
}