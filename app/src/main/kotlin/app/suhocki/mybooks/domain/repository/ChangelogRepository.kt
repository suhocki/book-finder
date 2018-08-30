package app.suhocki.mybooks.domain.repository

import app.suhocki.mybooks.domain.model.Changelog

interface ChangelogRepository {
    fun getChangelog(): List<Changelog>
}