package app.suhocki.mybooks.presentation.books.adapter

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
import app.suhocki.mybooks.*
import app.suhocki.mybooks.domain.model.Book
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.tintedImageView
import org.jetbrains.anko.cardview.v7.themedCardView
import org.jetbrains.anko.sdk25.coroutines.onClick

class BookItemUI : AnkoComponent<ViewGroup> {

    private lateinit var name: TextView
    private lateinit var price: TextView
    private lateinit var icon: ImageView
    lateinit var parent: ViewGroup

    var book: Book? = null
        set(value) {
            field = value
            Picasso.get().load(book!!.iconLink).into(icon)
            name.text = book!!.shortName
            price.text = parent.context.getString(R.string.rubles, book!!.price)
        }


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
                        textAppearance = R.style.TextAppearance_AppCompat_Body1
                    }.lparams(wrapContent, sp(38)) {
                        weight = 1f
                        setMargins(0, dip(8), 0, dip(8))
                    }

                    frameLayout {
                        textView {
                            price = this
                            setPadding(dip(8), 0, 0, dip(8))
                            textAppearance = R.style.TextAppearance_AppCompat_Body2
                        }.lparams(wrapContent, wrapContent) {
                            gravity = Gravity.BOTTOM or Gravity.START
                        }

                        tintedImageView(R.drawable.ic_buy) {
                            backgroundResource =
                                    context.attrResource(R.attr.selectableItemBackgroundBorderless)
                            padding = dip(8)
                            val colorPrimary = ContextCompat.getColor(context, R.color.colorPrimary)
                            setColorFilter(colorPrimary, PorterDuff.Mode.SRC_IN)
                            onClick {
                                Analytics.bookAddedToCart(book!!)
                                this@tintedImageView.context.openLink(book!!.website)
                            }
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