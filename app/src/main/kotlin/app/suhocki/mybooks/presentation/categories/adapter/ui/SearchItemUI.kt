package app.suhocki.mybooks.presentation.categories.adapter.ui

import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import app.suhocki.mybooks.R
import app.suhocki.mybooks.attrResource
import app.suhocki.mybooks.setForegroundCompat
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.themedCardView
import javax.inject.Inject

class SearchItemUI @Inject constructor() : AnkoComponent<ViewGroup> {
    lateinit var parent: View

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {

        frameLayout {
            this@SearchItemUI.parent = this

            themedCardView {
                useCompatPadding = true

                frameLayout {
                    setForegroundCompat(context.attrResource(R.attr.selectableItemBackground))

                    textView(R.string.search) {
                        textAppearance = R.style.TextAppearance_AppCompat_Caption
                        leftPadding = dip(8)
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

                }.lparams(matchParent, dip(38))

            }.lparams(matchParent, wrapContent) {
                setMargins(dip(8), 0, dip(8), 0)
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