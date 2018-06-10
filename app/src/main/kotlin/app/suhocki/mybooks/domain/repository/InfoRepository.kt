package app.suhocki.mybooks.domain.repository

interface InfoRepository {

    fun getContactPhones(): Set<String>

    fun getContactEmail(): String

    fun getOrganizationName(): String

    fun getWebsite(): Pair<String, String>

    fun getVkGroup(): Pair<String, String>

    fun getFacebook(): Pair<String, String>

    fun getAddress(): String

    fun getWorkingTime(): String
}