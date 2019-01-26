package app.suhocki.mybooks.ui.drawer.navigation.delegate

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.ui.drawer.navigation.delegate.CaptionAdapterDelegate.ViewHolder
import app.suhocki.mybooks.ui.drawer.navigation.entity.Caption
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import org.jetbrains.anko.*

class CaptionAdapterDelegate :
    AbsListItemAdapterDelegate<Caption, Any, ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup
    ) = ViewHolder(Ui(parent.context))

    override fun isForViewType(
        item: Any, items: MutableList<Any>,
        position: Int
    ) = items[position] is Caption

    override fun onBindViewHolder(
        item: Caption,
        holder: ViewHolder,
        payloads: MutableList<Any>
    ) = holder.bind(item)

    inner class ViewHolder(val ui: Ui) :
        RecyclerView.ViewHolder(ui.parentView) {

        fun bind(item: Caption) {
            ui.textView.textResource = item.textRes
        }
    }

    inner class Ui(context: Context) : AnkoComponent<Context> {
        lateinit var parentView: View
        lateinit var textView: TextView

        init {
            createView(AnkoContext.create(context, context, false))
        }

        override fun createView(ui: AnkoContext<Context>): View {
            parentView = ui.verticalLayout {

                frameLayout {
                    backgroundColorResource = R.color.colorDarkerGray
                }.lparams(matchParent, dip(1))

                textView {
                    textView = this
                    gravity = Gravity.CENTER_VERTICAL
                    textAppearance = R.style.TextAppearance_AppCompat_Caption
                }.lparams(matchParent, matchParent) {
                    leftMargin = dip(16)
                }
            }.apply {
                rootView.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    dimenAttr(R.attr.actionBarSize)
                )
            }
            return parentView
        }
    }
}