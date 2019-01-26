package app.suhocki.mybooks.ui.drawer.navigation.delegate

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.ui.drawer.navigation.entity.MenuItem
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class MenuItemAdapterDelegate(
    private val onMenuItemClick: (menuItem: MenuItem) -> Unit
) : AbsListItemAdapterDelegate<MenuItem, Any, RecyclerView.ViewHolder>() {

    private val ui by lazy {
        object : AnkoComponent<Context> {
            lateinit var parentView: View
            lateinit var textView: TextView

            override fun createView(ui: AnkoContext<Context>): View {
                return ui.linearLayout {
                    parentView = this
                    backgroundResource = R.drawable.bg_menu_selector

                    textView {
                        textView = this
                        gravity = Gravity.CENTER_VERTICAL
                        textAppearance = R.style.TextAppearance_AppCompat_Body2
                    }.lparams(matchParent, matchParent) {
                        leftMargin = dip(16)
                    }
                }.apply {
                    rootView.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dimenAttr(R.attr.actionBarSize)
                    )
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
    ) = items[position] is MenuItem

    override fun onBindViewHolder(
        item: MenuItem,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.itemView.onClick { onMenuItemClick(item) }
        holder.itemView.isSelected = item.isSelected

        val drawableStart = ContextCompat.getDrawable(holder.itemView.context, item.iconRes)
        ui.textView.setCompoundDrawablesWithIntrinsicBounds(drawableStart, null, null, null)
        ui.textView.textResource = item.nameRes
    }
}