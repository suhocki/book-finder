package app.suhocki.mybooks.ui.catalog.delegate

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.support.v4.widget.TextViewCompat
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.attrResource
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.setForegroundCompat
import app.suhocki.mybooks.ui.base.textViewCompat
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import org.jetbrains.anko.*

class CategoryAdapterDelegate(
    private val onCategoryClick: (Category) -> Unit
) : AbsListItemAdapterDelegate<Category, Any, CategoryAdapterDelegate.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup
    ) = ViewHolder(Ui(parent.context))

    override fun isForViewType(
        item: Any, items: MutableList<Any>,
        position: Int
    ) = items[position] is Category

    override fun onBindViewHolder(
        item: Category,
        holder: CategoryAdapterDelegate.ViewHolder,
        payloads: MutableList<Any>
    ) = holder.bind(item)

    inner class ViewHolder(
        val ui: CategoryAdapterDelegate.Ui
    ) : RecyclerView.ViewHolder(ui.parent) {
        private lateinit var category: Category

        init {
            itemView.setOnClickListener { onCategoryClick(category) }
        }

        fun bind(category: Category) {
            this.category = category
            with(ui) {
                name.text = category.name
                booksCount.text = category.booksCount.toString()
            }
        }
    }

    inner class Ui(context: Context) : AnkoComponent<Context> {
        lateinit var parent: View
        lateinit var name: TextView
        lateinit var booksCount: TextView

        init {
            createView(AnkoContext.create(context, context, false))
        }

        override fun createView(ui: AnkoContext<Context>) =
            ui.frameLayout {
                this@Ui.parent = this
                setForegroundCompat(context.attrResource(R.attr.selectableItemBackground))
                backgroundColor = Color.WHITE

                linearLayout {

                    textViewCompat {
                        booksCount = this
                        gravity = Gravity.CENTER
                        maxLines = 1
                        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(
                            this, 1, 14, 1, TypedValue.COMPLEX_UNIT_SP
                        )
                    }.lparams(dip(20), matchParent)

                    textView {
                        name = this
                        gravity = Gravity.CENTER_VERTICAL
                        setTypeface(typeface, Typeface.BOLD)
                        textAppearance = R.style.TextAppearance_AppCompat_Subhead
                        maxLines = 1
                        ellipsize = TextUtils.TruncateAt.END
                    }.lparams(matchParent, matchParent) {
                        leftMargin = dip(16)
                    }
                }.lparams(matchParent, dimenAttr(R.attr.actionBarSize)) {
                    leftMargin = dip(16)
                    rightMargin = dip(16)
                }

                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
    }
}