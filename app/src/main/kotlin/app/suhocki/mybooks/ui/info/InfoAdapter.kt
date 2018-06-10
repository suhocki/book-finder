package app.suhocki.mybooks.ui.info

import android.support.v7.recyclerview.extensions.EndActionAsyncDifferConfig
import android.support.v7.recyclerview.extensions.EndActionAsyncListDiffer
import app.suhocki.mybooks.ui.base.EndActionAdapterListUpdateCallback
import app.suhocki.mybooks.ui.catalog.delegate.HeaderAdapterDelegate
import app.suhocki.mybooks.ui.info.delegate.InfoAdapterDelegate
import app.suhocki.mybooks.ui.info.listener.OnInfoClickListener
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter

class InfoAdapter(
    onInfoClickListener: OnInfoClickListener
) : ListDelegationAdapter<MutableList<Any>>() {

    private val listUpdateCallback by lazy { EndActionAdapterListUpdateCallback(this) }

    private val diffConfig by lazy {
        EndActionAsyncDifferConfig.Builder<Any>(InfoDiffCallback()).build()
    }

    private val differ by lazy { EndActionAsyncListDiffer(listUpdateCallback, diffConfig) }

    init {
        delegatesManager
            .addDelegate(HeaderAdapterDelegate())
            .addDelegate(InfoAdapterDelegate(onInfoClickListener))
    }

    override fun getItemCount(): Int =
        differ.currentList.size

    fun submitList(list: List<Any>, onAnimationEnd: () -> Unit = {}) {
        listUpdateCallback.endAction = onAnimationEnd
        mutableListOf<Any>().apply {
            addAll(list)
            items = this
            differ.submitList(this)
        }
    }
}