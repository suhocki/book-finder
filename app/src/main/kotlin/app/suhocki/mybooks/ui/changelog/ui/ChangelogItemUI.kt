package app.suhocki.mybooks.ui.changelog.ui

import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.attrResource
import app.suhocki.mybooks.setForegroundCompat
import org.jetbrains.anko.*

class ChangelogItemUI : AnkoComponent<ViewGroup> {

    lateinit var parent: ViewGroup
    lateinit var date: TextView
    lateinit var version: TextView
    lateinit var download: View

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {

        verticalLayout {
            this@ChangelogItemUI.parent = this
            setForegroundCompat(context.attrResource(R.attr.selectableItemBackground))
            backgroundColorResource = R.color.colorWhite
            padding = dip(16)

            linearLayout {

                verticalLayout {
                    textView {
                        date = this
                        ellipsize = TextUtils.TruncateAt.END
                        maxLines = 1
                        textAppearance = R.style.TextAppearance_AppCompat_Body2
                    }.lparams(wrapContent, wrapContent) {
                        weight = 1f
                    }

                    textView {
                        version = this
                        ellipsize = TextUtils.TruncateAt.END
                        maxLines = 1
                        textAppearance = R.style.TextAppearance_AppCompat_Body1
                    }.lparams(wrapContent, wrapContent) {
                        weight = 1f
                        topMargin = dip(4)
                    }
                }.lparams(0, ViewGroup.LayoutParams.WRAP_CONTENT) {
                    weight = 1F
                }

                imageView(R.drawable.ic_download_file) {
                    download = this

                    padding = dimen(R.dimen.padding_toolbar_icon)
                    backgroundResource = context
                        .attrResource(R.attr.selectableItemBackgroundBorderless)
                }.lparams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }
    }.apply {
        rootView.layoutParams = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    fun setChanges(changes: Array<String>) {
        val oldViews = mutableListOf<View>()
        parent.childrenSequence().forEach {
            if (it.tag == TEMPORARY_TEXT_VIEW) oldViews.add(it)
        }
        oldViews.forEach { parent.removeView(it) }

        val ankoContext = AnkoContext.createReusable(parent.context, parent, false)

        changes.forEach {
            val textView = ankoContext.textView(it)
            textView.tag = TEMPORARY_TEXT_VIEW
            parent.addView(textView)
        }
    }

    companion object {
        private const val TEMPORARY_TEXT_VIEW = "TEMPORARY_TEXT_VIEW"
    }
}
