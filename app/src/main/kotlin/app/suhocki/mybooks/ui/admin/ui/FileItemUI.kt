package app.suhocki.mybooks.ui.admin.ui

import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.attrResource
import app.suhocki.mybooks.setForegroundCompat
import app.suhocki.mybooks.ui.Ids
import org.jetbrains.anko.*

class FileItemUI : AnkoComponent<ViewGroup> {

    lateinit var parent: ViewGroup
    lateinit var name: TextView
    lateinit var size: TextView
    lateinit var date: TextView

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {

        relativeLayout {
            this@FileItemUI.parent = this
            setForegroundCompat(context.attrResource(R.attr.selectableItemBackground))
            backgroundColorResource = R.color.colorWhite
            padding = dip(16)
            val idName = View.generateViewId()
            val idSize = View.generateViewId()
            val idDate = View.generateViewId()

            textView {
                id = idName
                name = this
                ellipsize = TextUtils.TruncateAt.END
                maxLines = 1
                textAppearance = R.style.TextAppearance_AppCompat_Body2
            }.lparams(wrapContent, wrapContent) {
                leftOf(idDate)
                alignParentLeft()
            }

            textView {
                id = idSize
                size = this
                ellipsize = TextUtils.TruncateAt.END
                maxLines = 1
                textAppearance = R.style.TextAppearance_AppCompat_Body1
                topPadding = dip(4)
            }.lparams(wrapContent, wrapContent) {
                below(idName)
            }

            textView {
                id = idDate
                date = this
                ellipsize = TextUtils.TruncateAt.END
                maxLines = 1
                textAppearance = R.style.TextAppearance_AppCompat_Body1
                leftPadding = dip(12)
            }.lparams(wrapContent, wrapContent) {
                alignParentRight()
            }
        }
    }.apply {
        rootView.layoutParams = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}
