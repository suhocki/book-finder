package app.suhocki.mybooks.ui.filter.ui

import android.support.v7.widget.RecyclerView
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import app.suhocki.mybooks.R
import org.jetbrains.anko.*

class FilterPriceItemUI : AnkoComponent<ViewGroup> {
    lateinit var parent: View
    lateinit var from: EditText
    lateinit var to: EditText

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {

        linearLayout {
            this@FilterPriceItemUI.parent = this
            backgroundResource = R.color.colorWhite

            linearLayout {

                textView(R.string.from) {
                    textAppearance = R.style.TextAppearance_AppCompat_Body1
                }

                editText {
                    from = this
                    textSizeDimen = R.dimen.size_text_search
                    maxLines = 1
                    isFocusable = true
                    isFocusableInTouchMode = true
                    gravity = Gravity.CENTER_VERTICAL
                    leftPadding = dip(8)
                    inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                    imeOptions = EditorInfo.IME_ACTION_NEXT
                    isVerticalScrollBarEnabled = false
                }.lparams(matchParent, matchParent)

            }.lparams(0, matchParent) {
                weight = 0.5f
                rightMargin = dip(8)
            }

            linearLayout {

                textView(R.string.to) {
                    textAppearance = R.style.TextAppearance_AppCompat_Body1
                }

                editText {
                    to = this
                    textSizeDimen = R.dimen.size_text_search
                    maxLines = 1
                    gravity = Gravity.CENTER_VERTICAL
                    isFocusable = true
                    leftPadding = dip(8)
                    isFocusableInTouchMode = true
                    inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                    imeOptions = EditorInfo.IME_ACTION_DONE
                    isVerticalScrollBarEnabled = false
                }.lparams(matchParent, matchParent)

            }.lparams(0, matchParent) {
                weight = 0.5f
                leftMargin = dip(8)
            }
        }.apply {
            rootView.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dimenAttr(R.attr.actionBarSize)
            ).apply {
                horizontalPadding = dip(16)
            }
        }
    }
}