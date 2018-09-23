package app.suhocki.mybooks.ui.admin

import android.support.v7.util.EndActionDiffUtil
import app.suhocki.mybooks.domain.model.admin.File

internal class AdminDiffCallback : EndActionDiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return if (oldItem is File && newItem is File) oldItem.id == newItem.id
        else false
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return if (oldItem is File && newItem is File) oldItem.name == newItem.name
        else false
    }
}