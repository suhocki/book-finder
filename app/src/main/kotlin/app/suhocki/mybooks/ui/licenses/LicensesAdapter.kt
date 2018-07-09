package app.suhocki.mybooks.ui.licenses

import android.support.v7.recyclerview.extensions.EndActionAsyncDifferConfig
import android.support.v7.recyclerview.extensions.EndActionAsyncListDiffer
import app.suhocki.mybooks.ui.base.EndActionAdapterListUpdateCallback
import app.suhocki.mybooks.ui.books.BooksDiffCallback
import app.suhocki.mybooks.ui.licenses.delegate.LicenseAdapterDelegate
import app.suhocki.mybooks.ui.licenses.listener.OnLicenseClickListener
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter


class LicensesAdapter(
    onLicenseClickListener: OnLicenseClickListener
) : ListDelegationAdapter<MutableList<Any>>() {

    private val listUpdateCallback by lazy { EndActionAdapterListUpdateCallback(this, null) }

    private val diffConfig by lazy { EndActionAsyncDifferConfig.Builder<Any>(BooksDiffCallback()).build() }

    private val differ by lazy { EndActionAsyncListDiffer(listUpdateCallback, diffConfig) }

    init {
        delegatesManager.addDelegate(LicenseAdapterDelegate(onLicenseClickListener))
    }

    override fun getItemCount(): Int =
        differ.currentList.size

    fun submitList(list: List<Any>) {
        mutableListOf<Any>().apply {
            addAll(list)
            items = this
            differ.submitList(this)
        }
    }
}