package app.suhocki.mybooks.ui.base.search

import android.support.v7.widget.RecyclerView
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.attrResource
import app.suhocki.mybooks.setForegroundCompat
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.themedCardView

class SearchItemUI : AnkoComponent<ViewGroup> {
    lateinit var parent: View
    lateinit var editText: TextView
    lateinit var startSearch: View

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {

        frameLayout {
            this@SearchItemUI.parent = this
            padding = dimen(R.dimen.padding_item_search)

            themedCardView {
                useCompatPadding = true
                maxCardElevation = 1f

                linearLayout {
                    setForegroundCompat(context.attrResource(R.attr.selectableItemBackground))

                    editText {
                        id = R.id.id_search
                        editText = this
                        leftPadding = dip(14)
                        rightPadding = dip(14)
                        backgroundColorResource = android.R.color.transparent
                        textSizeDimen = R.dimen.size_text_search
                        maxLines = 1
                        isFocusable = true
                        isFocusableInTouchMode = true
                        inputType = InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE
                        imeOptions = EditorInfo.IME_ACTION_SEARCH
                        isVerticalScrollBarEnabled = false
                    }.lparams(0, matchParent) {
                        weight = 1f
                        gravity = Gravity.CENTER_VERTICAL
                    }

                    imageView(R.drawable.ic_search) {
                        startSearch = this
                        setForegroundCompat(context.attrResource(R.attr.selectableItemBackgroundBorderless))
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