package app.suhocki.mybooks.ui.admin.ui

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.attrResource
import app.suhocki.mybooks.setForegroundCompat
import app.suhocki.mybooks.ui.base.materialButton
import org.jetbrains.anko.*

class UploadControlItemUI : AnkoComponent<ViewGroup> {

    lateinit var parent: ViewGroup

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

            materialButton {
                text = resources.getString(R.string.cancel)
                setTextAppearance(android.R.style.TextAppearance_Material_Button)
                includeFontPadding = true
            }
        }
    }.apply {
        rootView.layoutParams = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}
