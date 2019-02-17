package app.suhocki.mybooks.ui.books.delegate

import android.content.Context
import android.graphics.Point
import android.graphics.PorterDuff
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.attrResource
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.setForegroundCompat
import app.suhocki.mybooks.toRoundedPrice
import app.suhocki.mybooks.ui.base.entity.UiBook
import app.suhocki.mybooks.ui.base.materialCardView
import app.suhocki.mybooks.ui.base.simpleDraweeView
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.view.SimpleDraweeView
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.tintedImageView

class BookAdapterDelegate(
    private val onBookClick: (Book) -> Unit
) : AbsListItemAdapterDelegate<UiBook, Any, BookAdapterDelegate.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup
    ) = ViewHolder(Ui(parent.context))

    override fun isForViewType(
        item: Any, items: MutableList<Any>,
        position: Int
    ) = items[position] is UiBook

    override fun onBindViewHolder(
        item: UiBook,
        holder: BookAdapterDelegate.ViewHolder,
        payloads: MutableList<Any>
    ) = holder.bind(item, payloads)

    inner class ViewHolder(val ui: Ui) : RecyclerView.ViewHolder(ui.parent) {
        private lateinit var book: UiBook

        init {
            itemView.setOnClickListener { onBookClick(book) }
//            ui.buy.setOnClickListener { onBookClickListener.onBuyBookClick(book) }
        }

        fun bind(book: UiBook, payloads: MutableList<Any>) {
            this.book = book
            with(ui) {
                icon.setImageURI(book.iconLink)
                name.text = book.shortName
                price.text = parent.context.getString(R.string.rubles, book.price.toRoundedPrice())
                buy.setImageResource(book.buyDrawableRes)
                if (payloads.firstOrNull() is Int) {
                    val drawableRes = payloads.first() as Int
                    buy.setImageResource(drawableRes)
                    book.buyDrawableRes = drawableRes
                }
            }
        }
    }

    inner class Ui(private val context: Context) : AnkoComponent<Context> {

        lateinit var parent: ViewGroup
        lateinit var name: TextView
        lateinit var price: TextView
        lateinit var icon: SimpleDraweeView
        lateinit var buy: ImageView

        private var windowHeight = 0

        init {
            createView(AnkoContext.create(context, context, false))
        }

        override fun createView(ui: AnkoContext<Context>) = with(ui) {
            calculateWindowHeight(owner)

            frameLayout {
                this@Ui.parent = this

                materialCardView {
                    strokeColor = ResourcesCompat.getColor(
                        resources, R.color.colorDarkGray, context.theme
                    )
                    strokeWidth = dip(1)
                    radius = dip(10).toFloat()
                    cardElevation = 0f

                    verticalLayout {
                        setForegroundCompat(context.attrResource(R.attr.selectableItemBackground))

                        simpleDraweeView {
                            hierarchy.actualImageScaleType = ScalingUtils.ScaleType.FIT_CENTER
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
                                val colorPrimary =
                                    ContextCompat.getColor(context, R.color.colorPrimary)
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
}