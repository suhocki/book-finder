package app.suhocki.mybooks.ui.changelog.delegate

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import app.suhocki.mybooks.R
import app.suhocki.mybooks.domain.model.Changelog
import app.suhocki.mybooks.ui.changelog.listener.OnDownloadFileClickListener
import app.suhocki.mybooks.ui.changelog.ui.ChangelogItemUI
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import org.jetbrains.anko.AnkoContext
import java.text.SimpleDateFormat
import java.util.*


class ChangelogAdapterDelegate(
    private val onDownloadFileClickListener: OnDownloadFileClickListener
) : AdapterDelegate<MutableList<Any>>() {

    private val formatter by lazy { SimpleDateFormat(DATE_FORMAT, Locale.getDefault()) }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ChangelogItemUI()
            .apply { createView(AnkoContext.createReusable(parent.context, parent, false)) }
            .let { ViewHolder(it) }

    override fun isForViewType(items: MutableList<Any>, position: Int): Boolean =
        with(items[position]) { this is Changelog }

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (holder as ViewHolder).bind(items[position] as Changelog)


    private inner class ViewHolder(val ui: ChangelogItemUI) : RecyclerView.ViewHolder(ui.parent) {
        private lateinit var changelog: Changelog

        init {
            ui.download.setOnClickListener {
                val url = changelog.link
                url?.let { onDownloadFileClickListener.onDownloadFile(url) }
            }
        }

        fun bind(changelog: Changelog) {
            this.changelog = changelog
            with(ui) {
                version.text = formatter.format(Date(changelog.date * 1000L))
                date.text =
                        ui.parent.resources.getString(R.string.version_changelog, changelog.version)
                setChanges(changelog.changes)
                download.visibility =
                        if (changelog.link == null) View.INVISIBLE
                        else View.VISIBLE
            }
        }
    }

    companion object {
        private const val DATE_FORMAT = "dd MMM yyyy"
    }
}