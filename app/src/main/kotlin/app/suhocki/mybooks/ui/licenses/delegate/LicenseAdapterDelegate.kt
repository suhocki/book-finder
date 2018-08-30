package app.suhocki.mybooks.ui.licenses.delegate

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import app.suhocki.mybooks.domain.model.License
import app.suhocki.mybooks.getHumanName
import app.suhocki.mybooks.ui.changelog.listener.OnDownloadFileClickListener
import app.suhocki.mybooks.ui.licenses.listener.OnLicenseClickListener
import app.suhocki.mybooks.ui.licenses.ui.LicenseItemUI
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import org.jetbrains.anko.AnkoContext



class LicenseAdapterDelegate(
    private val onLicenseClickListener: OnLicenseClickListener
) : AdapterDelegate<MutableList<Any>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        LicenseItemUI()
            .apply { createView(AnkoContext.createReusable(parent.context, parent, false)) }
            .let { ViewHolder(it) }

    override fun isForViewType(items: MutableList<Any>, position: Int): Boolean =
        with(items[position]) { this is License }

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (holder as ViewHolder).bind(items[position] as License)


    private inner class ViewHolder(val ui: LicenseItemUI) : RecyclerView.ViewHolder(ui.parent) {
        private lateinit var license: License

        init {
            itemView.setOnClickListener { onLicenseClickListener.onLicenseClick(license) }
        }

        fun bind(license: License) {
            this.license = license
                ui.name.text = license.name
                ui.license.text = license.license.getHumanName(ui.parent.resources)
        }
    }
}