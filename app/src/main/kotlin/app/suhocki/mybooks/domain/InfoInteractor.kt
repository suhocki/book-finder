package app.suhocki.mybooks.domain

import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.mapper.Mapper
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.di.Room
import app.suhocki.mybooks.domain.model.Header
import app.suhocki.mybooks.domain.model.Info
import app.suhocki.mybooks.domain.repository.InfoRepository
import app.suhocki.mybooks.domain.repository.SettingsRepository
import javax.inject.Inject

class InfoInteractor @Inject constructor(
    @Room private val infoRepository: InfoRepository,
    private val settingsRepository: SettingsRepository,
    private val resourceManager: ResourceManager,
    private val mapper: Mapper
) {

    fun toogleAdminMode(): Boolean {
        settingsRepository.isAdminModeEnabled = !settingsRepository.isAdminModeEnabled
        return settingsRepository.isAdminModeEnabled
    }

    fun getShopInfoItems() =
        mutableListOf<Any>().apply {
            infoRepository.getShopInfo()?.let {
                addAll(mapper.map<List<Any>>(it))
            }
        }

    fun getAboutThisApplication() = listOf(
        HeaderEntity(
            resourceManager.getString(R.string.about_this_application),
            true
        ),
        InfoEntity(
            Info.InfoType.LICENSES,
            resourceManager.getString(R.string.licences),
            iconRes = R.drawable.ic_copyright
        ),
        InfoEntity(
            Info.InfoType.CHANGELOG,
            resourceManager.getString(R.string.changelog),
            iconRes = R.drawable.ic_changelog
        ),
        InfoEntity(
            Info.InfoType.ABOUT_DEVELOPER,
            resourceManager.getString(R.string.about_developer),
            iconRes = R.drawable.ic_developer
        )
    )

    inner class HeaderEntity(
        override var title: String,
        override val inverseColors: Boolean = false,
        override val allCaps: Boolean = true
    ) : Header

    class InfoEntity(
        override val type: Info.InfoType,
        override val name: String,
        override val valueForNavigation: String? = null,
        override val iconRes: Int = 0
    ) : Info
}