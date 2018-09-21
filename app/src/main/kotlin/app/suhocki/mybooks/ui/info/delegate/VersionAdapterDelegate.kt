package app.suhocki.mybooks.ui.info.delegate

import android.support.v7.widget.RecyclerView
import android.view.HapticFeedbackConstants
import android.view.ViewGroup
import app.suhocki.mybooks.R
import app.suhocki.mybooks.domain.model.Version
import app.suhocki.mybooks.ui.info.ui.VersionItemUI
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.sdk25.coroutines.onLongClick

class VersionAdapterDelegate(
    private val onVersionLongClick: () -> Unit
) : AdapterDelegate<MutableList<Any>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        VersionItemUI()
            .apply { createView(AnkoContext.createReusable(parent.context, parent, false)) }
            .let { ViewHolder(it) }

    override fun isForViewType(items: MutableList<Any>, position: Int): Boolean =
        with(items[position]) { this is Version }

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (holder as ViewHolder).bind(items[position] as Version)


    private inner class ViewHolder(val ui: VersionItemUI) : RecyclerView.ViewHolder(ui.parent) {

        init {
            ui.parent.onLongClick {
                ui.parent.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                onVersionLongClick()
            }
        }

        fun bind(version: Version) {
            with(ui) {
                versionName.text = ui.parent.resources.getString(
                    R.string.version,
                    version.version,
                    version.code
                )
            }
        }
    }
}