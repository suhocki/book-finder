package app.suhocki.mybooks.ui.licenses

import android.support.v7.util.DiffUtil
import app.suhocki.mybooks.domain.model.Header
import app.suhocki.mybooks.domain.model.License
import app.suhocki.mybooks.ui.admin.delegate.HeaderAdapterDelegate
import app.suhocki.mybooks.ui.licenses.delegate.LicenseAdapterDelegate
import com.hannesdorfmann.adapterdelegates3.AsyncListDifferDelegationAdapter


class LicensesAdapter(
    diffCallback: LicensesDiffCallback,
    onLicenseClick: (License) -> Unit
) : AsyncListDifferDelegationAdapter<Any>(diffCallback) {

    init {
        delegatesManager.addDelegate(LicenseAdapterDelegate(onLicenseClick))
        delegatesManager.addDelegate(HeaderAdapterDelegate())
    }

    fun setData(list: List<Any>) {
        items = list.toList()
    }

    class LicensesDiffCallback : DiffUtil.ItemCallback<Any>() {

        override fun areItemsTheSame(oldItem: Any, newItem: Any) = when {
            oldItem is License && newItem is License -> oldItem.url == newItem.url
            else -> oldItem::class.java == newItem::class.java
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any) = when {
            oldItem is License && newItem is License ->
                oldItem.license == newItem.license &&
                        oldItem.url == newItem.url &&
                        oldItem.name == newItem.name

            oldItem is Header && newItem is Header ->
                oldItem.title == newItem.title

            else -> true
        }
    }
}