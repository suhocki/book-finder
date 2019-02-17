package app.suhocki.mybooks.ui.drawer.navigation

import android.support.v7.util.DiffUtil
import app.suhocki.mybooks.ui.drawer.navigation.delegate.CaptionAdapterDelegate
import app.suhocki.mybooks.ui.drawer.navigation.delegate.DrawerHeaderAdapterDelegate
import app.suhocki.mybooks.ui.drawer.navigation.delegate.MenuItemAdapterDelegate
import app.suhocki.mybooks.ui.drawer.navigation.entity.Caption
import app.suhocki.mybooks.ui.drawer.navigation.entity.MenuItem
import com.hannesdorfmann.adapterdelegates3.AsyncListDifferDelegationAdapter

class NavigationDrawerAdapter(
    diffCallback: NavigationDrawerDiffCallback,
    onMenuItemClick: (menuItemId: Int) -> Unit,
    onCaptionLongClick: () -> Unit
) : AsyncListDifferDelegationAdapter<Any>(diffCallback) {

    init {
        delegatesManager
            .addDelegate(MenuItemAdapterDelegate(onMenuItemClick))
            .addDelegate(DrawerHeaderAdapterDelegate())
            .addDelegate(CaptionAdapterDelegate(onCaptionLongClick))
    }

    fun setData(data: List<Any>) {
        items = data.toList()
    }

    class NavigationDrawerDiffCallback : DiffUtil.ItemCallback<Any>() {

        override fun areItemsTheSame(oldItem: Any, newItem: Any) = when {
            oldItem is MenuItem && newItem is MenuItem -> oldItem.id == newItem.id

            else -> oldItem::class.java == newItem::class.java
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any) = when {
            oldItem is MenuItem && newItem is MenuItem ->
                oldItem.isSelected == newItem.isSelected &&
                        oldItem.iconRes == newItem.iconRes &&
                        oldItem.nameRes == newItem.nameRes

            oldItem is Caption && newItem is Caption ->
                oldItem.text == newItem.text && oldItem.clickable == newItem.clickable

            else -> true
        }
    }
}