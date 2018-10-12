package app.suhocki.mybooks.ui.admin.ui

import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.ViewGroup
import app.suhocki.mybooks.R
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.themedProgressBar

class ProgressItemUI : AnkoComponent<ViewGroup> {

    lateinit var parent: ViewGroup

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {

        frameLayout {
            this@ProgressItemUI.parent = this

            themedProgressBar(R.style.AccentProgressBar).lparams {
                gravity = Gravity.CENTER_HORIZONTAL
            }
        }.apply {
            rootView.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }
}
