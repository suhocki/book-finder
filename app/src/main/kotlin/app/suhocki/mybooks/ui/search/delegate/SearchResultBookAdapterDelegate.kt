package app.suhocki.mybooks.ui.search.delegate

import android.content.Context
import android.graphics.PorterDuff
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.attrResource
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.domain.model.BookSearchResult
import app.suhocki.mybooks.setForegroundCompat
import app.suhocki.mybooks.toRoundedPrice
import app.suhocki.mybooks.ui.base.materialCardView
import app.suhocki.mybooks.ui.base.simpleDraweeView
import com.facebook.drawee.view.SimpleDraweeView
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.tintedImageView

class BookSearchResultAdapterDelegate(
    private val onBookClickListener: (Book) -> Unit
) : AbsListItemAdapterDelegate<BookSearchResult, Any, BookSearchResultAdapterDelegate.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup
    ) = ViewHolder(Ui(parent.context))

    override fun isForViewType(
        item: Any, items: MutableList<Any>,
        position: Int
    ) = items[position] is BookSearchResult

    override fun onBindViewHolder(
        item: BookSearchResult,
        holder: BookSearchResultAdapterDelegate.ViewHolder,
        payloads: MutableList<Any>
    ) = holder.bind(item, payloads)

    inner class ViewHolder(val ui: Ui) :
        RecyclerView.ViewHolder(ui.parent) {
        private lateinit var book: Book

        init {
            itemView.setOnClickListener { onBookClickListener.invoke(book) }
//            ui.buy.setOnClickListener { onBookClickListener.onBuyBookClick(book) }
        }

        fun bind(searchResult: BookSearchResult, payloads: MutableList<Any>) {
            this.book = searchResult.book
            with(ui) {
                if (payloads.isNotEmpty()) {
                    val drawableRes = payloads.first() as Int
                    buy.setImageResource(drawableRes)
                }
                price.text = parent.context.getString(R.string.rubles, book.price.toRoundedPrice())
                foundBy.text = searchResult.foundBy
                foundBy.text = searchResult.foundBy
                bookName.text = searchResult.book.shortName
                bookImage.setImageURI(searchResult.book.productLink)
            }
        }
    }

    inner class Ui(context: Context) : AnkoComponent<Context> {
        lateinit var parent: View
        lateinit var foundBy: TextView
        lateinit var bookName: TextView
        lateinit var bookImage: SimpleDraweeView
        lateinit var price: TextView
        lateinit var buy: ImageView

        init {
            createView(AnkoContext.create(context, context, false))
        }

        override fun createView(ui: AnkoContext<Context>) =
            ui.frameLayout {
                this@Ui.parent = this
                horizontalPadding = dimen(R.dimen.padding_item_search)
                lparams(matchParent, dimen(R.dimen.height_search_result))

                materialCardView {
                    strokeColor = ResourcesCompat.getColor(
                        resources, R.color.colorDarkGray, context.theme
                    )
                    strokeWidth = dip(1)
                    radius = dip(10).toFloat()
                    cardElevation = 0f

                    frameLayout {
                        setForegroundCompat(context.attrResource(R.attr.selectableItemBackground))

                        verticalLayout {
                            weightSum = 8f

                            textView {
                                bookName = this
                                ellipsize = TextUtils.TruncateAt.END
                                maxLines = 2
                                textAppearance = R.style.TextAppearance_AppCompat_Body2
                                horizontalPadding = dip(14)
                            }.lparams(matchParent, 0) {
                                weight = 3f
                            }

                            textView {
                                foundBy = this
                                ellipsize = TextUtils.TruncateAt.END
                                maxLines = 1
                                gravity = Gravity.TOP
                                horizontalPadding = dip(14)
                            }.lparams(matchParent, 0) {
                                gravity = Gravity.BOTTOM
                                weight = 2f
                            }

                            frameLayout {
                                horizontalPadding = dip(14)

                                textView {
                                    price = this
                                    textAppearance = R.style.TextAppearance_AppCompat
                                }.lparams(wrapContent, wrapContent) {
                                    gravity = Gravity.CENTER_VERTICAL
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
                                    gravity = Gravity.CENTER_VERTICAL or Gravity.END
                                }
                            }.lparams(matchParent, 0) {
                                weight = 3f
                            }

                        }.lparams(matchParent, matchParent) {
                            setMargins(dimen(R.dimen.width_search_result_image), dip(8), 0, dip(8))
                        }

                        simpleDraweeView {
                            bookImage = this
                            scaleType = ImageView.ScaleType.CENTER_CROP
                        }.lparams(dimen(R.dimen.width_search_result_image), matchParent)

                    }

                }.lparams(matchParent, matchParent) {
                    gravity = Gravity.CENTER_VERTICAL
                }
            }
    }

}