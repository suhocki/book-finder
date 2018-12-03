package app.suhocki.mybooks.di

import javax.inject.Qualifier

@Qualifier annotation class Room
@Qualifier annotation class Firestore
@Qualifier annotation class ErrorReceiver
@Qualifier annotation class CatalogRequestFactory
@Qualifier annotation class DownloadDirectoryPath
@Qualifier annotation class CategoriesDecoration
@Qualifier annotation class SearchDecoration
@Qualifier annotation class SearchAuthor
@Qualifier annotation class SearchPublisher
@Qualifier annotation class SearchKey
@Qualifier annotation class SearchAll
@Qualifier annotation class IsSearchMode
@Qualifier annotation class CategoryId
@Qualifier annotation class BookId
@Qualifier annotation class Converters
