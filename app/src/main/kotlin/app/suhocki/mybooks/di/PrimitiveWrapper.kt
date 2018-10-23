package app.suhocki.mybooks.di

/**
 * https://youtrack.jetbrains.com/issue/KT-18918
 */
data class PrimitiveWrapper<out T>(val value: T)