package app.suhocki.mybooks.ui.admin

import android.support.v7.recyclerview.extensions.EndActionAsyncDifferConfig
import android.support.v7.recyclerview.extensions.EndActionAsyncListDiffer
import app.suhocki.mybooks.domain.model.admin.File
import app.suhocki.mybooks.ui.admin.delegate.FileAdapterDelegate
import app.suhocki.mybooks.ui.admin.delegate.UploadControlAdapterDelegate
import app.suhocki.mybooks.ui.base.EndActionAdapterListUpdateCallback
import app.suhocki.mybooks.ui.catalog.delegate.HeaderAdapterDelegate
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter


class AdminAdapter(
    onFileClick: (File) -> Unit
) : ListDelegationAdapter<MutableList<Any>>() {

    init {
        delegatesManager.addDelegate(FileAdapterDelegate(onFileClick))
        delegatesManager.addDelegate(UploadControlAdapterDelegate())
        delegatesManager.addDelegate(HeaderAdapterDelegate())
    }

    private val listUpdateCallback by lazy { EndActionAdapterListUpdateCallback(this, null) }

    private val diffConfig by lazy {
        EndActionAsyncDifferConfig.Builder<Any>(AdminDiffCallback()).build()
    }

    private val differ by lazy { EndActionAsyncListDiffer(listUpdateCallback, diffConfig) }

    override fun getItemCount(): Int =
        differ.currentList.size

    fun submitList(
        list: List<Any>,
        changedPosition: Int = UNDEFINED,
        payload: Any? = null
    ) = mutableListOf<Any>().apply {
        addAll(list)
        items = this
        if (changedPosition != UNDEFINED) {
            notifyItemChanged(changedPosition, payload)
        } else differ.submitList(this)
    }


    companion object {
        const val UNDEFINED = -1
    }
}