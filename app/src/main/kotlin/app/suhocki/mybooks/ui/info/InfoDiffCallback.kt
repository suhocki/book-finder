package app.suhocki.mybooks.ui.info

import android.support.v7.util.EndActionDiffUtil
import app.suhocki.mybooks.domain.model.Info

internal class InfoDiffCallback : EndActionDiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return if (oldItem is Info && newItem is Info)
            oldItem.name == newItem.name && oldItem.type == newItem.type
        else oldItem::class.java == newItem::class.java
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return if (oldItem is Info && newItem is Info) oldItem == newItem
        else false
    }

    override fun getChangePayload(oldItem: Any?, newItem: Any?): Any {
        return Any()
    }
}