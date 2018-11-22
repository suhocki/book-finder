package app.suhocki.mybooks.ui.catalog.entity

import app.suhocki.mybooks.domain.model.Header


class HeaderEntity(
    override var title: String,
    override val inverseColors: Boolean = false,
    override val allCaps: Boolean = true
) : Header {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HeaderEntity

        if (title != other.title) return false
        if (inverseColors != other.inverseColors) return false
        if (allCaps != other.allCaps) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + inverseColors.hashCode()
        result = 31 * result + allCaps.hashCode()
        return result
    }
}