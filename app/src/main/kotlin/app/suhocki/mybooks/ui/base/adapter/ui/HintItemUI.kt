package app.suhocki.mybooks.ui.base.adapter.ui

import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import app.suhocki.mybooks.R
import org.jetbrains.anko.*

class HintItemUI : AnkoComponent<ViewGroup> {
    lateinit var parent: View
    lateinit var textView: TextView

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {

        frameLayout {
            this@HintItemUI.parent = this

            textView {
                this@HintItemUI.textView = this
                gravity = Gravity.CENTER
            }.lparams(matchParent, matchParent)
        }.apply {
            rootView.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dimen(R.dimen.height_item_hint)
            )
        }
    }
}