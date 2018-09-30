package app.suhocki.mybooks.domain.model.admin

import java.io.Serializable

interface File : Serializable {
    val name: String
    val id: String
    val humanFileSize: String
    val fileSize: Long
    val date: String
}