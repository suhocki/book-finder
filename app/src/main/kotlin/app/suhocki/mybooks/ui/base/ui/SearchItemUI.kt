package app.suhocki.mybooks.ui.base.ui

import android.support.v4.content.res.ResourcesCompat
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
import app.suhocki.mybooks.ui.Ids
import app.suhocki.mybooks.ui.base.materialCardView
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.themedCardView

class SearchItemUI : AnkoComponent<ViewGroup> {
    lateinit var parent: View
    lateinit var editText: TextView
    lateinit var startSearch: View

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {

        frameLayout {
            this@SearchItemUI.parent = this
            backgroundColorResource = R.color.colorWhite
            verticalPadding = dimen(R.dimen.padding_item_search)

            materialCardView {
                strokeColor = ResourcesCompat.getColor(
                    resources, R.color.colorDarkGray, context.theme
                )
                strokeWidth = dip(1)
                radius = dip(10).toFloat()
                cardElevation = 0f

                linearLayout {
                    setForegroundCompat(context.attrResource(R.attr.selectableItemBackground))

                    editText {
                        id = Ids.search
                        editText = this
                        horizontalPadding = dip(12)
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
                        backgroundColorResource = R.color.colorPrimary
                        padding = dip(8)
                    }.lparams(
                        dimenAttr(R.attr.actionBarSize) - dimen(R.dimen.padding_item_search) - dip(
                            4
                        ), matchParent
                    ) {
                        gravity = Gravity.CENTER_VERTICAL or Gravity.END
                    }

                }

            }.lparams(matchParent, matchParent) {
                gravity = Gravity.CENTER_VERTICAL
                horizontalMargin = dip(4)
            }
        }.apply {
            rootView.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dimenAttr(R.attr.actionBarSize)
            )
        }
    }
}