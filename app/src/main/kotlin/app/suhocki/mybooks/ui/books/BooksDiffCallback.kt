package app.suhocki.mybooks.ui.books

import android.support.v7.util.EndActionDiffUtil
import app.suhocki.mybooks.domain.model.Book

internal class BooksDiffCallback : EndActionDiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return if (oldItem is Book && newItem is Book) oldItem.id == newItem.id
        else false
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any) =
        when {
            oldItem is Book && newItem is Book ->
                oldItem.shortName == newItem.shortName &&
                        oldItem.price == newItem.price &&
                        oldItem.iconLink == newItem.iconLink

            else -> false
        }

    override fun getChangePayload(oldItem: Any?, newItem: Any?): Any {
        return Any()
    }
}