package app.suhocki.mybooks.ui.drawer.navigation.delegate

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
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
    ) {
    }

    inner class ViewHolder(val ui: Ui) :
        RecyclerView.ViewHolder(ui.parentView)

    inner class Ui(context: Context) : AnkoComponent<Context> {
        lateinit var parentView: View

        init {
            createView(AnkoContext.create(context, context, false))
        }

        override fun createView(ui: AnkoContext<Context>): View {
            return ui.imageView(R.mipmap.ic_launcher_round) {
                parentView = this
            }.apply {
                val lparams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                lparams.gravity = Gravity.CENTER_HORIZONTAL
                lparams.verticalMargin = dip(24)
                rootView.layoutParams = lparams
            }
        }
    }
}