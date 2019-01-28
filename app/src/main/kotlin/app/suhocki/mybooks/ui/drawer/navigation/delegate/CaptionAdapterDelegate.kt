package app.suhocki.mybooks.ui.drawer.navigation.delegate

import android.content.Context
import android.support.v4.content.res.ResourcesCompat
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
        RecyclerView.ViewHolder(ui.parent) {

        fun bind(item: Caption) {
            ui.textView.text = item.text
        }
    }

    inner class Ui(context: Context) : AnkoComponent<Context> {
        lateinit var parent: View
        lateinit var textView: TextView

        init {
            createView(AnkoContext.create(context, context, false))
        }

        override fun createView(ui: AnkoContext<Context>): View {
            parent = ui.verticalLayout {
                lparams(matchParent)

                frameLayout {
                    backgroundColorResource = R.color.colorDarkGray
                }.lparams(matchParent, dip(2)) {
                    topMargin = dip(4)
                }

                textView {
                    textView = this
                    typeface = ResourcesCompat.getFont(context, R.font.roboto)
                    gravity = Gravity.CENTER_VERTICAL
                    textColorResource = R.color.colorDarkerGray
                }.lparams(matchParent, matchParent) {
                    leftMargin = dip(16)
                    verticalMargin = dip(20)
                }
            }
            return parent
        }
    }
}