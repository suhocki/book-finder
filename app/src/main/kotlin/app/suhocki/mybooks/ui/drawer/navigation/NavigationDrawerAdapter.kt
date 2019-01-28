package app.suhocki.mybooks.ui.drawer.navigation

import app.suhocki.mybooks.ui.drawer.navigation.delegate.CaptionAdapterDelegate
import app.suhocki.mybooks.ui.drawer.navigation.delegate.DrawerHeaderAdapterDelegate
import app.suhocki.mybooks.ui.drawer.navigation.delegate.MenuItemAdapterDelegate
import app.suhocki.mybooks.ui.drawer.navigation.entity.MenuItem
import com.hannesdorfmann.adapterdelegates3.AbsDelegationAdapter

class NavigationDrawerAdapter(
    onMenuItemClick: (menuItem: MenuItem) -> Unit
) : AbsDelegationAdapter<List<Any>>() {

    init {
        delegatesManager.apply {
            addDelegate(MenuItemAdapterDelegate(onMenuItemClick))
            addDelegate(DrawerHeaderAdapterDelegate())
            addDelegate(CaptionAdapterDelegate())
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setData(data: List<Any>) {
        setItems(data.toList())
        notifyDataSetChanged()
    }
}