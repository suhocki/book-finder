package app.suhocki.mybooks.ui.admin

import android.support.v7.recyclerview.extensions.EndActionAsyncDifferConfig
import android.support.v7.recyclerview.extensions.EndActionAsyncListDiffer
import app.suhocki.mybooks.ui.admin.delegate.FileAdapterDelegate
import app.suhocki.mybooks.ui.base.EndActionAdapterListUpdateCallback
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter


class AdminAdapter : ListDelegationAdapter<MutableList<Any>>() {

    private val listUpdateCallback by lazy { EndActionAdapterListUpdateCallback(this, null) }

    private val diffConfig by lazy {
        EndActionAsyncDifferConfig.Builder<Any>(AdminDiffCallback()).build()
    }

    private val differ by lazy { EndActionAsyncListDiffer(listUpdateCallback, diffConfig) }

    init {
        delegatesManager.addDelegate(FileAdapterDelegate())
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