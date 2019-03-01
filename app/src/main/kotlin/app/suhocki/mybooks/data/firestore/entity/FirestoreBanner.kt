package app.suhocki.mybooks.data.firestore.entity

import app.suhocki.mybooks.domain.model.Banner

data class FirestoreBanner(
    override var id: String = "",
    override var imageUrl: String = "",
    override var description: String = ""
) : Banner