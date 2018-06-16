package app.suhocki.mybooks.domain.repository

interface InfoRepository {

    fun getContactPhones(): Set<String>

    fun setContactPhones(phones: Set<String>)

    fun getContactEmail(): String

    fun setContactEmail(email: String)

    fun getOrganizationName(): String

    fun setOrganizationName(name: String)

    fun getWebsite(): Pair<String, String>

    fun setWebsite(website: String)

    fun getVkGroup(): Pair<String, String>

    fun setVkGroup(url: String)

    fun getFacebook(): Pair<String, String>

    fun setFacebook(url: String)

    fun getAddress(): String

    fun setAddress(address: String)

    fun getWorkingTime(): String

    fun setWorkingTime(time: String)
}