package app.suhocki.mybooks.ui.base.adapter.ui

import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.attrResource
import app.suhocki.mybooks.setForegroundCompat
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.themedCardView

class SearchItemUI : AnkoComponent<ViewGroup> {
    lateinit var parent: View
    lateinit var hint: TextView

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {

        frameLayout {
            this@SearchItemUI.parent = this
            padding = dimen(R.dimen.padding_item_search)

            themedCardView {
                useCompatPadding = true

                frameLayout {
                    setForegroundCompat(context.attrResource(R.attr.selectableItemBackground))

                    textView {
                        this@SearchItemUI.hint = this
                        textAppearance = R.style.TextAppearance_AppCompat_Caption
                        leftPadding = dip(14)
                        textSize = 14f
                    }.lparams(wrapContent, wrapContent) {
                        gravity = Gravity.CENTER_VERTICAL
                    }

                    imageView(R.drawable.ic_search) {
                        backgroundResource = R.drawable.search_icon_background
                        padding = dip(8)
                    }.lparams(dip(38), matchParent) {
                        gravity = Gravity.CENTER_VERTICAL or Gravity.END
                    }

                }.lparams(matchParent, matchParent)

            }.lparams(matchParent, matchParent) {
                gravity = Gravity.CENTER_VERTICAL
            }
        }.apply {
            rootView.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dimenAttr(R.attr.actionBarSize)
            )

        }
    }
}