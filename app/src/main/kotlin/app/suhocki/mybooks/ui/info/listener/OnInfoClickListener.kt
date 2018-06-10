package app.suhocki.mybooks.ui.info.listener

import app.suhocki.mybooks.domain.model.Info

interface OnInfoClickListener {
    fun onInfoClick(info: Info)
}