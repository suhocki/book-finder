package app.suhocki.mybooks.ui.admin

import android.support.v7.util.EndActionDiffUtil
import app.suhocki.mybooks.domain.model.Header
import app.suhocki.mybooks.domain.model.admin.File
import app.suhocki.mybooks.domain.model.admin.UploadControl

internal class AdminDiffCallback : EndActionDiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return if (oldItem is File && newItem is File) oldItem.id == newItem.id
        else oldItem::class.java == newItem::class.java
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return if (oldItem is File && newItem is File) oldItem.name == newItem.name
        else if (oldItem is UploadControl && newItem is UploadControl) {
            oldItem.progress == newItem.progress && oldItem.stepRes == oldItem.stepRes
        } else if (oldItem is Header && newItem is Header) oldItem.title == newItem.title
        else false
    }

    override fun getChangePayload(oldItem: Any?, newItem: Any?): Any {
        return Any()
    }
}