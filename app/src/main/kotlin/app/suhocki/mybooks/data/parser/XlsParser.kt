package app.suhocki.mybooks.data.parser

import app.suhocki.mybooks.data.room.entity.BookEntity
import app.suhocki.mybooks.data.room.entity.CategoryEntity
import app.suhocki.mybooks.data.notification.NotificationHelper
import app.suhocki.mybooks.data.parser.entity.BannerEntity
import app.suhocki.mybooks.data.parser.entity.InfoEntity
import app.suhocki.mybooks.data.parser.entity.StatisticsEntity
import app.suhocki.mybooks.data.parser.entity.XlsDocumentEntity
import app.suhocki.mybooks.di.module.UploadServiceModule
import app.suhocki.mybooks.domain.model.Banner
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.model.Info
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject


class XlsParser @Inject constructor(
    private val uploadControl: UploadServiceModule.UploadControlEntity,
    private val notificationHelper: NotificationHelper
) : AnkoLogger {

    private lateinit var xlsFileName: String
    private lateinit var xlsFileCreationDate: String
    private val xlsFileColumnNames = mutableListOf<String>()

    fun parseStructure(xlsFile: File): ArrayList<String> {
        val contentString = getStringFromFile(xlsFile)
        val allMatches = ArrayList<String>()
        val matcher = Pattern.compile(REGEX_XLS_DATA).matcher(contentString)
        val contentStringLength = contentString.length.toFloat()
        var progress: Int

        while (matcher.find()) {
            progress = (matcher.start() / contentStringLength * 100).toInt()

            uploadControl.sendProgress(progress, notificationHelper)
            matcher.group().let {
                allMatches.add(
                    it.removeSurrounding(REGEX_XLS_CDATA_START, REGEX_XLS_CDATA_END)
                        .replace("&quot;", "\"")
                )
            }
        }
        return allMatches
    }

    fun extractPayload(strings: ArrayList<String>): XlsDocumentEntity {
        val statisticsData = mutableMapOf<Category, StatisticsEntity>()
        var currentCategory: CategoryEntity? = null

        val booksData = mutableMapOf<CategoryEntity, MutableList<BookEntity>>()
        val bookFieldsQueue = ArrayDeque<String>()

        val banners = mutableListOf<Banner>()
        val bannerFieldsQueue = ArrayDeque<String>()

        val contacts = mutableListOf<Info>()
        var contactSearchPosition = 0

        var mergeCount = 0
        var isHeaderFound = false
        var currentXlsPage = ""

        xlsFileColumnNames.clear()

        for (index in 0 until strings.size) {
            val currentWord = strings[index]

            if (currentWord == LIST_BOOKS) {
                currentXlsPage = LIST_BOOKS
                continue
            } else if (currentWord == LIST_BANNERS) {
                currentXlsPage = LIST_BANNERS
                continue
            } else if (currentWord == LIST_CONTACTS) {
                currentXlsPage = LIST_CONTACTS
                continue
            }

            if (currentXlsPage == LIST_BOOKS) {
                if (currentWord == MERGE) {
                    mergeCount++
                    isHeaderFound = true
                    continue
                } else if (currentWord in STATUS_TYPES &&
                    index in 1 until strings.lastIndex &&
                    strings[index - 1] == MERGE && strings[index + 1] == MERGE
                ) {
                    isHeaderFound = true
                    continue
                }

                if (mergeCount == 1) {
                    xlsFileName = currentWord
                    continue
                }

                if (mergeCount == 2) {
                    xlsFileCreationDate = currentWord
                    continue
                }

                if (mergeCount == 3) {
                    xlsFileColumnNames.add(currentWord)
                    continue
                }

                if (currentWord == "&lt;товары отсутствуют&gt;") {
                    isHeaderFound = false
                    continue
                }

                if (strings[index - 1] == currentCategory?.name &&
                    currentWord in STATUS_TYPES
                ) {
                    continue
                }

                if (isHeaderFound) {
                    isHeaderFound = false
                    bookFieldsQueue.clear()
                    currentCategory = CategoryEntity(currentWord)
                    booksData[currentCategory] = mutableListOf()
                    statisticsData[currentCategory] = StatisticsEntity()
                    continue
                }

                bookFieldsQueue.add(currentWord)

                if (bookFieldsQueue.size == xlsFileColumnNames.size) {
                    info { bookFieldsQueue.first }
                    booksData[currentCategory]!!
                        .add(createBookEntity(currentCategory, bookFieldsQueue, statisticsData))
                        .also {
                            val progress = (index / strings.size.toDouble() * 100).toInt()
                            uploadControl.sendProgress(progress, notificationHelper)
                        }
                }
            } else if (currentXlsPage == LIST_BANNERS) {
                bannerFieldsQueue.add(currentWord)
                if (bannerFieldsQueue.size == 2) {
                    banners.add(BannerEntity(bannerFieldsQueue.pop(), bannerFieldsQueue.pop()))
                }
            } else if (currentXlsPage == LIST_CONTACTS) {

                when {
                    currentWord.startsWith("375") ->
                        contacts.add(InfoEntity(Info.InfoType.PHONE, currentWord))

                    currentWord.startsWith("mailto") -> {
                        val infoEntity = InfoEntity(
                            Info.InfoType.EMAIL,
                            currentWord.replace("mailto:", "")
                        )
                        contacts.add(infoEntity)
                    }

                    currentWord == "mybooks.by" ->
                        contacts.add(InfoEntity(Info.InfoType.WEBSITE, currentWord))

                    currentWord.startsWith("https://www.facebook.com") ->
                        contacts.add(InfoEntity(Info.InfoType.FACEBOOK, currentWord))

                    currentWord.startsWith("https://vk.com") ->
                        contacts.add(InfoEntity(Info.InfoType.VK, currentWord))

                    contactSearchPosition == CONTACT_POSITION_ORGANIZATION ->
                        contacts.add(InfoEntity(Info.InfoType.ORGANIZATION, currentWord))

                    contactSearchPosition == CONTACT_POSITION_ADDRESS ->
                        contacts.add(InfoEntity(Info.InfoType.ADDRESS, currentWord))

                    contactSearchPosition == CONTACT_POSITION_WORKING_TIME ->
                        contacts.add(InfoEntity(Info.InfoType.WORKING_TIME, currentWord))
                }

                contactSearchPosition++
            }
        }

        return XlsDocumentEntity(
            title = strings[POSITION_TITLE],
            creationDate = strings[POSITION_CREATION_DATE],
            columnNames = xlsFileColumnNames,
            booksData = booksData.apply { forEach { it.key.booksCount = it.value.size } },
            statisticsData = statisticsData,
            bannersData = banners,
            infosData = contacts
        )
    }

    private fun createBookEntity(
        currentCategory: CategoryEntity?,
        objectFieldsQueue: ArrayDeque<String>,
        statisticsData: MutableMap<Category, StatisticsEntity>
    ): BookEntity {
        return BookEntity(
            category = currentCategory!!.name,
            shortName = objectFieldsQueue.pop(),
            fullName = objectFieldsQueue.pop(),
            shortDescription = objectFieldsQueue.pop(),
            fullDescription = objectFieldsQueue.pop(),
            price = objectFieldsQueue.pop().let { if (it.isNotBlank()) it else "100" }.toDouble(),
            iconLink = objectFieldsQueue.pop().replace(OLD_WEBSITE, NEW_WEBSITE),
            productLink = objectFieldsQueue.pop().replace(OLD_WEBSITE, NEW_WEBSITE),
            website = objectFieldsQueue.pop().replace(OLD_WEBSITE, NEW_WEBSITE),
            productCode = objectFieldsQueue.pop(),
            status = if (objectFieldsQueue.isNotEmpty()) objectFieldsQueue.pop() else null
        ).apply {
            publisher = findValue(KEY_PUBLISHER, shortDescription, fullDescription)
            author = if (currentCategory.name == "НОВИНКИ \"ЛАБИРИНТ\"") {
                val authorWithSpaces = REGEX_ISBN_NUMBER.toRegex().replace(shortDescription, "")
                    .replace(shortName, "")
                "^ *".toRegex().replace(authorWithSpaces, "")
            } else {
                findValue(KEY_AUTHOR, shortDescription, fullDescription)
            }
            cover = findValue(KEY_COVER, shortDescription, fullDescription)
            format = findValue(KEY_FORMAT, shortDescription, fullDescription)
            pageCount = findValue(KEY_PAGES, shortDescription, fullDescription)
            series = findValue(KEY_SERIES, shortDescription, fullDescription)
            year = findValue(KEY_YEAR, shortDescription, fullDescription)
            description = findValue(KEY_DESCR, shortDescription, fullDescription)
            statisticsData[currentCategory]!!.add(this)
        }
    }

    private fun findValue(key: String, vararg strings: String): String? {
        if (key == KEY_DESCR) {
            val fullDescription = strings[1]
            val lastIndexOfKey = fullDescription.lastIndexOfAny(KEYS_SET)
            val answer = if (lastIndexOfKey == -1) null
            else fullDescription.substring(lastIndexOfKey)
            return if (answer != null && answer.startsWith(KEY_ISBN)) {
                val matcher = Pattern.compile(REGEX_ISBN_NUMBER).matcher(answer)
                matcher.find()
                val isbnNumber = matcher.group()
                answer.removePrefix(KEY_ISBN).removePrefix(isbnNumber)
            } else if (answer != null && answer.startsWith(KEY_COVER)) {
                val startIndex = KEY_COVER.length + FORMAT_LENGTH
                if (answer.length > startIndex) answer.substring(startIndex) else null
            } else if (answer != null && answer.startsWith(KEY_YEAR)) {
                val startIndex = KEY_YEAR.length + YEAR_LENGTH
                if (answer.length > startIndex) answer.substring(startIndex) else null
            } else fullDescription
        }
        strings.forEach {
            val keyIndex = it.indexOf(key)
            if (keyIndex != -1) {
                val keysSet = mutableSetOf<String>().apply { addAll(KEYS_SET); remove(key) }
                val keyIndexLast = keyIndex + key.length
                val valueIndexLast = it.indexOfAny(keysSet, keyIndexLast)
                val answer = if (valueIndexLast == -1) it.substring(keyIndexLast)
                else it.substring(keyIndexLast, valueIndexLast)
                if (key == KEY_COVER) {
                    val endIndex =
                        if (answer.length <= FORMAT_LENGTH) answer.length
                        else FORMAT_LENGTH
                    return answer.substring(0, endIndex)
                } else if (key == KEY_YEAR) {
                    val endIndex =
                        if (answer.length <= YEAR_LENGTH) answer.length
                        else YEAR_LENGTH
                    return answer.substring(0, endIndex)
                }
                return answer
            }
        }
        return null
    }

    private fun getStringFromFile(file: File): String {
        val fin = file.inputStream()
        val ret = convertStreamToString(fin)
        fin.close()
        return ret
    }

    private fun convertStreamToString(inputStream: InputStream): String {
        val reader = BufferedReader(InputStreamReader(inputStream))
        return reader.readText().apply {
            reader.close()
        }
    }

    companion object {

        private const val LIST_BOOKS = "Прайс-лист 1"
        private const val LIST_BANNERS = "Лист2"
        private const val LIST_CONTACTS = "Лист3"

        private const val REGEX_ISBN_NUMBER = "[0-9-]+"
        private const val REGEX_XLS_DATA =
            "(?s)(?<=<Data ss:Type=\"String\">|<Data ss:Type=\"Number\">).*?(?=<\\/Data>)|section_title_1|(?s)(?<=ss:HRef=\").*?(?=\"><Data)|Merge|Прайс-лист 1|Лист2|Лист3"
        private const val REGEX_XLS_CDATA_START = "<![CDATA["
        private const val REGEX_XLS_CDATA_END = "]]>"
        private const val POSITION_TITLE = 1
        private const val POSITION_CREATION_DATE = 2
        private const val MERGE = "Merge"
        private const val OLD_WEBSITE = "mybooks.shop.by"
        private const val NEW_WEBSITE = "mybooks.by"
        private const val KEY_ISBN = "ISBN: "
        private const val KEY_AUTHOR = "Автор: "
        private const val KEY_PUBLISHER = "Издатель: "
        private const val KEY_SERIES = "Серия: "
        private const val KEY_FORMAT = "Формат: "
        private const val KEY_YEAR = "Год издания: "
        private const val KEY_PAGES = "Страниц.: "
        private const val KEY_COVER = "Обложка: "
        private const val KEY_DESCR = "KEY_DESCRIPTION"
        private const val FORMAT_LENGTH = 3
        private const val YEAR_LENGTH = 4
        private const val CONTACT_POSITION_ORGANIZATION = 0
        private const val CONTACT_POSITION_ADDRESS = 9
        private const val CONTACT_POSITION_WORKING_TIME = 10
        private val KEYS_SET = setOf(
            KEY_ISBN,
            KEY_AUTHOR,
            KEY_PUBLISHER,
            KEY_SERIES,
            KEY_FORMAT,
            KEY_YEAR,
            KEY_PAGES,
            KEY_COVER
        )
        private val STATUS_TYPES = setOf(
            "Доступен", "Доступно", "В наличии"
        )
    }
}