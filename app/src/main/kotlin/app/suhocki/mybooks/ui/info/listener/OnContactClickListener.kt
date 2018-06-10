package app.suhocki.mybooks.ui.info.listener

import app.suhocki.mybooks.domain.model.Contact

interface OnContactClickListener {
    fun onContactClick(contact: Contact)
}