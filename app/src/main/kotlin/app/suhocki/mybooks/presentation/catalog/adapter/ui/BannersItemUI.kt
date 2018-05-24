package app.suhocki.mybooks.presentation.catalog.adapter.ui

import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import app.suhocki.mybooks.R
import org.jetbrains.anko.*

class BannersItemUI : AnkoComponent<ViewGroup> {
    lateinit var parent: View
    lateinit var image: ImageView
    lateinit var description: TextView

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {

        frameLayout {
            this@BannersItemUI.parent = this

            imageView {
                this@BannersItemUI.image = this
                scaleType = ImageView.ScaleType.CENTER_CROP
            }.lparams(matchParent, matchParent)

            textView {
                this@BannersItemUI.description = this
                textAppearance = R.style.TextAppearance_AppCompat_Subhead
                backgroundColorResource = R.color.colorDescriptionBackground
                gravity = Gravity.CENTER_HORIZONTAL
            }.lparams(matchParent, wrapContent) {
                gravity = Gravity.BOTTOM
                bottomMargin = dip(8)
            }
        }.apply {
            rootView.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dip(112)
            )
        }
    }
}