package app.suhocki.mybooks.ui.drawer.navigation.delegate

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
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

        fun bind(item: MenuItem) = with(ui.textView) {
            this@ViewHolder.item = item
            val drawableStart = ContextCompat.getDrawable(context, item.iconRes)
            setCompoundDrawablesWithIntrinsicBounds(drawableStart, null, null, null)
            textResource = item.nameRes
            isSelected = item.isSelected
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
                textView = this
                backgroundResource = R.drawable.bg_menu_selector
                gravity = Gravity.CENTER_VERTICAL
                textAppearance = R.style.TextAppearance_AppCompat_Body2
            }.apply {
                val lparams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    dimenAttr(R.attr.actionBarSize)
                )
                lparams.leftMargin = dip(16)
                rootView.layoutParams = lparams
            }
            return parent
        }
    }
}