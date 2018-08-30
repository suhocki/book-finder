package app.suhocki.mybooks.domain

import app.suhocki.mybooks.domain.repository.ChangelogRepository
import javax.inject.Inject

class ChangelogInteractor @Inject constructor(
    private val changelogRepository: ChangelogRepository
) {

    fun getChangelog() =
        changelogRepository.getChangelog().sortedByDescending {
            it.date
        }
}