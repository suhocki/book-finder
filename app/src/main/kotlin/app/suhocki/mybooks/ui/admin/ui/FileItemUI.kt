package app.suhocki.mybooks.ui.admin.ui

import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.ViewGroup
import android.widget.TextView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.attrResource
import app.suhocki.mybooks.setForegroundCompat
import org.jetbrains.anko.*

class FileItemUI : AnkoComponent<ViewGroup> {

    lateinit var parent: ViewGroup
    lateinit var name: TextView

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {

        frameLayout {
            this@FileItemUI.parent = this
            setForegroundCompat(context.attrResource(R.attr.selectableItemBackground))
            backgroundColorResource = R.color.colorWhite
            padding = dip(16)

            textView {
                name = this
                ellipsize = TextUtils.TruncateAt.END
                maxLines = 1
                textAppearance = R.style.TextAppearance_AppCompat_Body2
            }.lparams(wrapContent, wrapContent)
        }
    }.apply {
        rootView.layoutParams = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}
