package app.suhocki.mybooks.ui.drawer.navigation.delegate

import android.content.Context
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.ui.drawer.navigation.delegate.DrawerHeaderAdapterDelegate.ViewHolder
import app.suhocki.mybooks.ui.drawer.navigation.entity.DrawerHeaderItem
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import org.jetbrains.anko.*

class DrawerHeaderAdapterDelegate :
    AbsListItemAdapterDelegate<DrawerHeaderItem, Any, ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup
    ) = ViewHolder(Ui(parent.context))

    override fun isForViewType(
        item: Any, items: MutableList<Any>,
        position: Int
    ) = items[position] is DrawerHeaderItem

    override fun onBindViewHolder(
        item: DrawerHeaderItem,
        holder: ViewHolder,
        payloads: MutableList<Any>
    ) = holder.bind(item)

    inner class ViewHolder(val ui: Ui) :
        RecyclerView.ViewHolder(ui.parent) {

        fun bind(item: DrawerHeaderItem) {
            ui.textView.textResource = item.textRes
        }
    }

    inner class Ui(context: Context) : AnkoComponent<Context> {
        lateinit var parent: View
        lateinit var textView: TextView

        init {
            createView(AnkoContext.create(context, false))
        }

        override fun createView(ui: AnkoContext<Context>): View {
            return ui.verticalLayout {
                this@Ui.parent = this

                imageView(R.mipmap.ic_launcher_round)
                    .lparams(matchParent, wrapContent) {
                        gravity = Gravity.CENTER_HORIZONTAL
                        verticalMargin = dip(24)
                    }

                textView {
                    textView = this
                    typeface = ResourcesCompat.getFont(context, R.font.roboto)
                    gravity = Gravity.CENTER_HORIZONTAL
                }.lparams(matchParent, wrapContent)

                frameLayout {
                    backgroundColorResource = R.color.colorDarkGray
                }.lparams(matchParent, dip(1)) {
                    topMargin = dip(24)
                    bottomMargin = dip(4)
                }

                lparams(matchParent)
            }
        }
    }
}