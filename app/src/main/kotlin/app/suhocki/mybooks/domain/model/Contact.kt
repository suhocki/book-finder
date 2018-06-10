package app.suhocki.mybooks.domain.model

interface Contact {
    val type: ContactType
    val name: String
    val valueForNavigation: String?
    val iconRes: Int

    enum class ContactType {
        PHONE,
        EMAIL,
        WEBSITE,
        FACEBOOK,
        VK,
    }
}