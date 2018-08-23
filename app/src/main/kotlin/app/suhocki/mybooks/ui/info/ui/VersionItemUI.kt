package app.suhocki.mybooks.ui.info.ui

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.R.string.name
import app.suhocki.mybooks.attrResource
import app.suhocki.mybooks.setForegroundCompat
import org.jetbrains.anko.*

class VersionItemUI : AnkoComponent<ViewGroup> {
    lateinit var parent: View
    lateinit var versionName: TextView

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {

        frameLayout {
            this@VersionItemUI.parent = this
            backgroundColor = Color.WHITE

            textView {
                versionName = this
                gravity = Gravity.CENTER
                textAppearance = R.style.TextAppearance_AppCompat_Caption
            }.lparams(matchParent, matchParent) {
                bottomMargin = dip(16)
            }
        }.apply {
            rootView.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }
}