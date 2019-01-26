package app.suhocki.mybooks.ui.drawer.navigation

import android.support.v7.util.DiffUtil
import app.suhocki.mybooks.ui.drawer.navigation.delegate.CaptionAdapterDelegate
import app.suhocki.mybooks.ui.drawer.navigation.delegate.DrawerHeaderAdapterDelegate
import app.suhocki.mybooks.ui.drawer.navigation.delegate.MenuItemAdapterDelegate
import app.suhocki.mybooks.ui.drawer.navigation.entity.MenuItem
import com.hannesdorfmann.adapterdelegates3.AsyncListDifferDelegationAdapter

class NavigationDrawerAdapter(
    diffCallback: ItemCallback,
    onMenuItemClick: (menuItem: MenuItem) -> Unit
) : AsyncListDifferDelegationAdapter<Any>(diffCallback) {

    init {
        delegatesManager.apply {
            addDelegate(MenuItemAdapterDelegate(onMenuItemClick))
            addDelegate(DrawerHeaderAdapterDelegate())
            addDelegate(CaptionAdapterDelegate())
        }
    }

    fun setData(data: List<Any>) {
        items = data.toList()
    }

    class ItemCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(old: Any, new: Any): Boolean {
            return if (old is MenuItem && new is MenuItem) old.id == new.id
            else true
        }

        override fun areContentsTheSame(old: Any, new: Any): Boolean {
            return if (old is MenuItem && new is MenuItem) old.isSelected == new.isSelected
            else true
        }

        override fun getChangePayload(oldItem: Any, newItem: Any): Any? {
            return Any()
        }
    }
}