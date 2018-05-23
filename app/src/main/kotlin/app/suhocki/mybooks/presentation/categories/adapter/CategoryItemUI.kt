package app.suhocki.mybooks.presentation.categories.adapter

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
import org.jetbrains.anko.*
import javax.inject.Inject

class CategoryItemUI @Inject constructor() : AnkoComponent<ViewGroup> {
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
            backgroundResource = context.attrResource(R.attr.selectableItemBackground)

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
                    textAppearance = R.style.TextAppearance_AppCompat_Body1
                }.lparams(matchParent, matchParent) {
                    leftMargin = dip(16)
                }
            }.lparams(matchParent, dip(48)) {
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