package app.suhocki.mybooks.domain.model.filter

import app.suhocki.mybooks.domain.model.Header

interface FilterCategory : Header {
    var isExpanded: Boolean
    var checkedCount: Int
}