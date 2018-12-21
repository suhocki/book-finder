package app.suhocki.mybooks.data.firestore

import android.support.annotation.StringDef

@StringDef(
    FirestoreRepository.BOOKS,
    FirestoreRepository.CATEGORIES,
    FirestoreRepository.BANNERS,
    FirestoreRepository.SHOP_INFO
)
@Retention(AnnotationRetention.SOURCE)
annotation class FirestoreCollection