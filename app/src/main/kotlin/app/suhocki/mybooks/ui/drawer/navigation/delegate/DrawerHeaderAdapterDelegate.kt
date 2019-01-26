package app.suhocki.mybooks.ui.drawer.navigation.delegate

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import app.suhocki.mybooks.R
import app.suhocki.mybooks.ui.drawer.navigation.entity.DrawerHeaderItem
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import org.jetbrains.anko.*

class DrawerHeaderAdapterDelegate : AbsListItemAdapterDelegate<DrawerHeaderItem, Any, RecyclerView.ViewHolder>() {

    private val ui by lazy {
        object : AnkoComponent<Context> {
            lateinit var parentView: View

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

    override fun onCreateViewHolder(
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        val context = parent.context
        ui.createView(AnkoContext.create(context, context, false))
        return object : RecyclerView.ViewHolder(ui.parentView) {}
    }

    override fun isForViewType(
        item: Any, items: MutableList<Any>,
        position: Int
    ) = items[position] is DrawerHeaderItem

    override fun onBindViewHolder(
        item: DrawerHeaderItem,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
    }
}