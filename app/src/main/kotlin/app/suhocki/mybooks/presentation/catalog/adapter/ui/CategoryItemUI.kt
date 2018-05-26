package app.suhocki.mybooks.presentation.catalog.adapter.ui

import android.graphics.Color
import android.support.v4.widget.TextViewCompat
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.attrResource
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.presentation.base.textViewCompat
import app.suhocki.mybooks.setForegroundCompat
import org.jetbrains.anko.*

class CategoryItemUI : AnkoComponent<ViewGroup> {
    lateinit var parent: View
    private lateinit var name: TextView
    private lateinit var booksCount: TextView

    var category: Category? = null
        set(value) {
            field = value
            name.text = category!!.name
            booksCount.text = category!!.booksCount.toString()
        }


    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {

        frameLayout {
            this@CategoryItemUI.parent = this
            setForegroundCompat(context.attrResource(R.attr.selectableItemBackground))
            backgroundColor = Color.WHITE

            linearLayout {

                textViewCompat {
                    booksCount = this
                    gravity = Gravity.CENTER
                    maxLines = 1
                    TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(
                        this, 1, 14, 1, TypedValue.COMPLEX_UNIT_SP
                    )
                }.lparams(dip(20), matchParent)

                textView {
                    name = this
                    gravity = Gravity.CENTER_VERTICAL
                    textAppearance = R.style.TextAppearance_AppCompat_Body2
                }.lparams(matchParent, matchParent) {
                    leftMargin = dip(16)
                }
            }.lparams(matchParent, dimenAttr(R.attr.actionBarSize)) {
                leftMargin = dip(16)
                rightMargin = dip(16)
            }
        }.apply {
            rootView.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }
}