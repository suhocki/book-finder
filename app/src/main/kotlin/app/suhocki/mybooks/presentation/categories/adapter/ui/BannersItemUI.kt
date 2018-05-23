package app.suhocki.mybooks.presentation.categories.adapter.ui

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import app.suhocki.mybooks.R
import org.jetbrains.anko.*
import javax.inject.Inject

class BannersItemUI @Inject constructor() : AnkoComponent<ViewGroup> {
    lateinit var parent: View

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {

        frameLayout {
            this@BannersItemUI.parent = this

            textView("Banners") {
                allCaps = true
                textAppearance = R.style.TextAppearance_AppCompat_Title
            }.lparams {
                margin = dip(8)
            }
        }.apply {
            rootView.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }
}