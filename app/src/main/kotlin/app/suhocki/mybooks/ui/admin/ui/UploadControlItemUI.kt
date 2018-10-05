package app.suhocki.mybooks.ui.admin.ui

import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import app.suhocki.mybooks.R
import app.suhocki.mybooks.attrResource
import org.jetbrains.anko.*

class UploadControlItemUI : AnkoComponent<ViewGroup> {

    lateinit var parent: ViewGroup
    lateinit var cancel: View

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {

        verticalLayout {
            this@UploadControlItemUI.parent = this

            backgroundColorResource = R.color.colorWhite
            padding = dip(16)

            resources.getStringArray(R.array.database_upload_steps).forEach {
                linearLayout {

                    textView {
                        text = it
                        ellipsize = TextUtils.TruncateAt.END
                        maxLines = 1
                        textAppearance = R.style.TextAppearance_AppCompat_Body1
                    }.lparams(0, wrapContent) {
                        weight = 1f
                        gravity = Gravity.CENTER_VERTICAL
                    }

                    frameLayout {
                        themedProgressBar(R.style.AccentProgressBar) {
                            visibility = View.INVISIBLE
                        }.lparams(dip(14), dip(14)) {
                            gravity = Gravity.END
                        }

                        imageView(R.drawable.ic_success) {
                            visibility = View.INVISIBLE
                            setColorFilter(
                                ContextCompat.getColor(context, R.color.colorGreen),
                                android.graphics.PorterDuff.Mode.SRC_IN
                            )
                        }.lparams(dip(14), dip(14)) {
                            gravity = Gravity.END
                        }

                        textView()
                    }.lparams(wrapContent) {
                        marginStart = dip(8)
                        gravity = Gravity.CENTER_VERTICAL
                    }
                }
            }

            textView(R.string.cancel) {
                cancel = this
                textAppearance = R.style.TextAppearance_AppCompat_Body2
                typeface = Typeface.DEFAULT_BOLD
                padding = dip(8)
                backgroundResource = context
                    .attrResource(R.attr.selectableItemBackgroundBorderless)
                textColorResource = R.color.colorDarkerGray
            }.lparams(wrapContent, wrapContent) {
                topMargin = dip(8)
                gravity = Gravity.CENTER_HORIZONTAL
            }
        }
    }.apply {
        rootView.layoutParams = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}
