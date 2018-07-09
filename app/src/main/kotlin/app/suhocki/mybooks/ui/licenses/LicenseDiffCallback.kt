package app.suhocki.mybooks.ui.licenses

import android.support.v7.util.EndActionDiffUtil
import app.suhocki.mybooks.domain.model.License

internal class LicenseDiffCallback : EndActionDiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return if (oldItem is License && newItem is License) oldItem.url == newItem.url
        else false
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return if (oldItem is License && newItem is License) oldItem.url == newItem.url
        else false
    }
}