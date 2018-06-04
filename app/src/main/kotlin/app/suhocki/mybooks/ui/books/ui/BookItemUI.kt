package app.suhocki.mybooks.ui.books.ui

import android.content.Context
import android.graphics.Point
import android.graphics.PorterDuff
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.attrResource
import app.suhocki.mybooks.setForegroundCompat
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.tintedImageView
import org.jetbrains.anko.cardview.v7.themedCardView

class BookItemUI : AnkoComponent<ViewGroup> {

    lateinit var parent: ViewGroup
    lateinit var name: TextView
    lateinit var price: TextView
    lateinit var icon: ImageView
    lateinit var buy: ImageView

    private var windowHeight = 0


    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        calculateWindowHeight(owner.context)

        frameLayout {
            this@BookItemUI.parent = this

            themedCardView {
                useCompatPadding = true

                verticalLayout {
                    setForegroundCompat(context.attrResource(R.attr.selectableItemBackground))

                    imageView {
                        icon = this
                    }.lparams(matchParent, dip(112)) {
                        setPadding(0, dip(8), 0, dip(8))
                    }

                    textView {
                        name = this
                        ellipsize = TextUtils.TruncateAt.END
                        maxLines = 2
                        textAppearance = R.style.TextAppearance_AppCompat_Body2
                    }.lparams(wrapContent, sp(38)) {
                        weight = 1f
                        setMargins(0, dip(8), 0, dip(8))
                    }

                    frameLayout {
                        textView {
                            price = this
                            setPadding(dip(8), 0, 0, dip(8))
                            textAppearance = R.style.TextAppearance_AppCompat
                        }.lparams(wrapContent, wrapContent) {
                            gravity = Gravity.BOTTOM or Gravity.START
                        }

                        tintedImageView(R.drawable.ic_buy) {
                            buy = this
                            backgroundResource =
                                    context.attrResource(R.attr.selectableItemBackgroundBorderless)
                            padding = dip(8)
                            val colorPrimary = ContextCompat.getColor(context, R.color.colorPrimary)
                            setColorFilter(colorPrimary, PorterDuff.Mode.SRC_IN)
                        }.lparams { gravity = Gravity.END }
                    }.lparams(matchParent, matchParent) {
                        padding = dip(8)
                    }
                }

            }.lparams(matchParent, wrapContent)
        }.apply {
            rootView.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    private fun calculateWindowHeight(owner: Context) {
        val display = owner.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        windowHeight = size.x
    }
}