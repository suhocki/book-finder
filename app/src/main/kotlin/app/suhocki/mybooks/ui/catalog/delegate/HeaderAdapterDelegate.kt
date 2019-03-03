package app.suhocki.mybooks.ui.catalog.delegate

import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.ui.catalog.entity.Header
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import org.jetbrains.anko.*

class HeaderAdapterDelegate :
    AbsListItemAdapterDelegate<Header, Any, HeaderAdapterDelegate.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup
    ) = ViewHolder(Ui(parent.context))

    override fun isForViewType(
        item: Any, items: MutableList<Any>,
        position: Int
    ) = items[position] is Header

    override fun onBindViewHolder(
        item: Header,
        holder: ViewHolder,
        payloads: MutableList<Any>
    ) = holder.bind(item)

    inner class ViewHolder(
        val ui: Ui
    ) : RecyclerView.ViewHolder(ui.parent) {
        fun bind(header: Header) {
            with(ui) {
                parent.backgroundResource = R.color.colorPrimary
                title.textAppearance = R.style.TextAppearance_AppCompat_Subhead_Inverse
                title.setTypeface(title.typeface, Typeface.BOLD)
                title.isAllCaps = true
                title.textResource = header.textRes
            }
        }
    }

    inner class Ui(context: Context) : AnkoComponent<Context> {
        lateinit var parent: View
        lateinit var title: TextView

        init {
            createView(AnkoContext.create(context, context, false))
        }

        override fun createView(ui: AnkoContext<Context>) = with(ui) {

            frameLayout {
                this@Ui.parent = this
                backgroundResource = R.color.colorWhite

                textView {
                    this@Ui.title = this
                    allCaps = true
                    textAppearance = R.style.TextAppearance_AppCompat_Subhead
                    setTypeface(typeface, Typeface.BOLD)
                    horizontalPadding = dip(18)
                    gravity = Gravity.CENTER_VERTICAL
                    minHeight = dimenAttr(R.attr.actionBarSize)
                }.lparams(wrapContent, wrapContent)

                lparams(matchParent, wrapContent)
            }
        }
    }
}