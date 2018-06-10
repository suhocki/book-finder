package app.suhocki.mybooks.domain.model

interface Info {
    val type: InfoType
    val name: String
    val valueForNavigation: String?
    val iconRes: Int

    enum class InfoType {
        PHONE,
        EMAIL,
        WEBSITE,
        FACEBOOK,
        VK,
        WORKING_TIME,
        ADDRESS,
    }
}