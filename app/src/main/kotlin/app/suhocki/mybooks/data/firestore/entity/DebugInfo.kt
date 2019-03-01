package app.suhocki.mybooks.data.firestore.entity

import app.suhocki.mybooks.data.firestore.FirestoreRepository

enum class DebugInfo(
    val text: String,
    var count: Int = 0,
    var visible: Boolean = false
) {
    BANNER(FirestoreRepository.BANNERS),
    BOOKS(FirestoreRepository.BOOKS),
    CATEGORIES(FirestoreRepository.CATEGORIES),
    SHOP_INFO(FirestoreRepository.SHOP_INFO)
}