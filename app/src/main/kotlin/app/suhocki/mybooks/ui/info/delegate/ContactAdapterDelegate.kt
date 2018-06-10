package app.suhocki.mybooks.ui.info.delegate

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import app.suhocki.mybooks.domain.model.Contact
import app.suhocki.mybooks.ui.info.listener.OnContactClickListener
import app.suhocki.mybooks.ui.info.ui.ContactItemUI
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import org.jetbrains.anko.AnkoContext

class ContactAdapterDelegate(
    private val onContactClickListener: OnContactClickListener
) : AdapterDelegate<MutableList<Any>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ContactItemUI()
            .apply { createView(AnkoContext.createReusable(parent.context, parent, false)) }
            .let { ViewHolder(it) }

    override fun isForViewType(items: MutableList<Any>, position: Int): Boolean =
        with(items[position]) { this is Contact }

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (holder as ViewHolder).bind(items[position] as Contact)


    private inner class ViewHolder(val ui: ContactItemUI) : RecyclerView.ViewHolder(ui.parent) {
        private lateinit var contact: Contact

        init {
            itemView.setOnClickListener { onContactClickListener.onContactClick(contact) }
        }

        fun bind(contact: Contact) {
            this.contact = contact
            with(ui) {
                name.text = contact.name
                icon.setImageResource(contact.iconRes)
            }
        }
    }
}