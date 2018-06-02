package app.suhocki.mybooks.ui.base.adapter.ui

import android.graphics.PorterDuff
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.attrResource
import app.suhocki.mybooks.setForegroundCompat
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.tintedImageView
import org.jetbrains.anko.cardview.v7.themedCardView
import kotlin.math.roundToInt

class SearchResultItemUI : AnkoComponent<ViewGroup> {
    lateinit var parent: View
    lateinit var foundBy: TextView
    lateinit var bookName: TextView
    lateinit var bookImage: ImageView
    lateinit var price: TextView
    lateinit var buy: View

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        val viewSize = (dimenAttr(R.attr.actionBarSize) * 2f).roundToInt()

        frameLayout {
            this@SearchResultItemUI.parent = this
            horizontalPadding = dimen(R.dimen.padding_item_search)

            themedCardView {
                useCompatPadding = true

                frameLayout {
                    setForegroundCompat(context.attrResource(R.attr.selectableItemBackground))

                    verticalLayout {
                        weightSum = 8f

                        textView {
                            bookName = this
                            ellipsize = TextUtils.TruncateAt.END
                            maxLines = 2
                            gravity = Gravity.BOTTOM
                            textAppearance = R.style.TextAppearance_AppCompat_Body2
                            horizontalPadding = dip(8)
                        }.lparams(matchParent, 0) {
                            marginEnd = dimen(R.dimen.width_search_book_image)
                            weight = 3f
                        }

                        textView {
                            foundBy = this
                            ellipsize = TextUtils.TruncateAt.END
                            maxLines = 1
                            gravity = Gravity.TOP
                            horizontalPadding = dip(8)
                        }.lparams(matchParent, 0) {
                            gravity = Gravity.BOTTOM
                            marginEnd = dimen(R.dimen.width_search_book_image)
                            weight = 2f
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
                                val colorPrimary =
                                    ContextCompat.getColor(context, R.color.colorPrimary)
                                setColorFilter(colorPrimary, PorterDuff.Mode.SRC_IN)
                            }.lparams {
                                horizontalMargin = dip(8)
                                gravity = Gravity.END
                            }
                        }.lparams(matchParent, 0) {
                            marginEnd = dimen(R.dimen.width_search_book_image)
                            padding = dip(8)
                            weight = 3f
                        }

                    }.lparams(matchParent, viewSize)

                    imageView {
                        bookImage = this
                        scaleType = ImageView.ScaleType.CENTER_CROP
                    }.lparams(dimen(R.dimen.width_search_book_image), viewSize) {
                        horizontalMargin = dip(8)
                        gravity = Gravity.END
                    }

                }.lparams(matchParent, matchParent)

            }.lparams(matchParent, matchParent) {
                gravity = Gravity.CENTER_VERTICAL
            }
        }.apply {
            rootView.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                wrapContent
            )

        }
    }
}