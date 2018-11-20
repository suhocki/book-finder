package app.suhocki.mybooks.domain.repository

import app.suhocki.mybooks.domain.model.ShopInfo

interface InfoRepository {

    fun getShopInfo(): ShopInfo? = throw NotImplementedError()

    fun setShopInfo(shopInfo: ShopInfo)
}