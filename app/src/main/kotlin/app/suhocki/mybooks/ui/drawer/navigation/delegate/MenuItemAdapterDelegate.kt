package app.suhocki.mybooks.ui.drawer.navigation.delegate

import android.content.Context
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.ui.drawer.navigation.delegate.MenuItemAdapterDelegate.ViewHolder
import app.suhocki.mybooks.ui.drawer.navigation.entity.MenuItem
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class MenuItemAdapterDelegate(
    private val onMenuItemClick: (menuItem: MenuItem) -> Unit
) : AbsListItemAdapterDelegate<MenuItem, Any, ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup
    ) = ViewHolder(Ui(parent.context))

    override fun isForViewType(
        item: Any, items: MutableList<Any>,
        position: Int
    ) = items[position] is MenuItem

    override fun onBindViewHolder(
        item: MenuItem,
        holder: ViewHolder,
        payloads: MutableList<Any>
    ) = holder.bind(item)

    inner class ViewHolder(val ui: Ui) : RecyclerView.ViewHolder(ui.textView) {
        private lateinit var item: MenuItem

        init {
            ui.parent.onClick { onMenuItemClick(item) }
        }

        fun bind(item: MenuItem) {
            with(ui.parent) {
                this@ViewHolder.item = item
                isSelected = item.isSelected

                with(ui.textView) {
                    textResource = item.nameRes

                    val icon = ResourcesCompat.getDrawable(resources, item.iconRes, context.theme)!!
                    setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null)
                    compoundDrawablePadding = dip(26)

                    setPadding(dip(16), 0, 0, 0)
                }
            }
        }
    }

    inner class Ui(context: Context) : AnkoComponent<Context> {
        lateinit var parent: View
        lateinit var textView: TextView

        init {
            createView(AnkoContext.create(context, context, false))
        }

        override fun createView(ui: AnkoContext<Context>): View {
            parent = ui.textView {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    dip(54)
                )

                textView = this
                typeface = ResourcesCompat.getFont(context, R.font.roboto_medium)
                backgroundResource = R.drawable.selector_menu_overlay

                ResourcesCompat
                    .getColorStateList(resources, R.color.selector_primary, context.theme)
                    .let { setTextColor(it) }

                gravity = Gravity.CENTER_VERTICAL
            }
            return parent
        }
    }
}