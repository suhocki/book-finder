package app.suhocki.mybooks.domain.repository

import app.suhocki.mybooks.domain.model.Banner

interface BannersRepository {

    fun getBanners(): List<Banner>

    fun setBanners(banners: List<Banner>)
}