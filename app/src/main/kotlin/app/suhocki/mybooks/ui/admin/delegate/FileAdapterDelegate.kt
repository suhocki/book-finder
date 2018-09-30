package app.suhocki.mybooks.ui.admin.delegate

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import app.suhocki.mybooks.domain.model.admin.File
import app.suhocki.mybooks.ui.admin.ui.FileItemUI
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.sdk25.coroutines.onClick


class FileAdapterDelegate(
    private val onFileClick: (File) -> Unit
) : AdapterDelegate<MutableList<Any>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(FileItemUI().apply {
            createView(AnkoContext.createReusable(parent.context, parent, false))
        })

    override fun isForViewType(items: MutableList<Any>, position: Int): Boolean =
        with(items[position]) { this is File }

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (holder as ViewHolder).bind(items[position] as File)


    private inner class ViewHolder(val ui: FileItemUI) : RecyclerView.ViewHolder(ui.parent) {
        private lateinit var file: File

        init {
            ui.parent.onClick { onFileClick(file) }
        }

        fun bind(file: File) {
            this.file = file
            ui.name.text = file.name
            ui.size.text = file.humanFileSize
            ui.date.text = file.date
        }
    }
}