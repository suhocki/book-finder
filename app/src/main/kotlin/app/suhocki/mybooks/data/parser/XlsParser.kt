package app.suhocki.mybooks.data.parser

import app.suhocki.mybooks.checkThreadInterrupt
import app.suhocki.mybooks.data.database.entity.BookEntity
import app.suhocki.mybooks.data.database.entity.CategoryEntity
import app.suhocki.mybooks.data.parser.entity.XlsDocumentEntity
import app.suhocki.mybooks.data.progress.ProgressHandler
import app.suhocki.mybooks.data.progress.ProgressStep
import org.jetbrains.anko.AnkoLogger
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject


class XlsParser @Inject constructor(
    private val progressListener: ProgressHandler
) : AnkoLogger {

    fun parseStructure(xlsFile: File): ArrayList<String> {
        val contentString = getStringFromFile(xlsFile)
        val allMatches = ArrayList<String>()
        val matcher = Pattern.compile(REGEX_XLS_DATA).matcher(contentString)

        while (matcher.find()) {
            checkThreadInterrupt()
            matcher.group().let {
                allMatches.add(it.removeSurrounding(REGEX_XLS_CDATA_START, REGEX_XLS_CDATA_END))
            }
        }
        return allMatches
    }

    fun extractPayload(strings: ArrayList<String>): XlsDocumentEntity {
        val booksData = mutableMapOf<CategoryEntity, MutableList<BookEntity>>()
        var categoryNamePosition = -1
        val objectFieldsQueue = ArrayDeque<String>()
        var currentCategory: CategoryEntity? = null
        val stepParsing = ProgressStep.PARSING

        for (index in POSITION_COLUMN_NAMES_END + 1 until strings.size) {
            if (index == categoryNamePosition) continue

            if (strings[index] == DATA_DELIMETER) {
                objectFieldsQueue.clear()
                categoryNamePosition = index + 1
                currentCategory = CategoryEntity(strings[categoryNamePosition])
                booksData[currentCategory] = mutableListOf()
                continue
            }

            with(objectFieldsQueue) {
                add(strings[index])
                if (size == OBJECT_FIELD_COUNT) {
                    checkThreadInterrupt()
                    booksData[currentCategory]!!.add(
                        BookEntity(
                            category = currentCategory!!.name,
                            shortName = pop(),
                            fullName = pop(),
                            shortDescription = pop(),
                            fullDescription = pop(),
                            price = pop().toDouble(),
                            iconLink = pop().replace(OLD_WEBSITE, NEW_WEBSITE),
                            productLink = pop().replace(OLD_WEBSITE, NEW_WEBSITE),
                            website = pop().replace(OLD_WEBSITE, NEW_WEBSITE),
                            productCode = pop(),
                            status = pop()
                        ).apply {
                            publisher = findValue(KEY_PUBLISHER, shortDescription, fullDescription)
                            author = findValue(KEY_AUTHOR, shortDescription, fullDescription)
                            cover = findValue(KEY_COVER, shortDescription, fullDescription)
                            format = findValue(KEY_FORMAT, shortDescription, fullDescription)
                            pageCount = findValue(KEY_PAGES, shortDescription, fullDescription)
                            series = findValue(KEY_SERIES, shortDescription, fullDescription)
                            year = findValue(KEY_YEAR, shortDescription, fullDescription)
                            description =
                                    findValue(KEY_DESCRIPTION, shortDescription, fullDescription)
                        }
                    ).also {
                        val progress = (index / strings.size.toDouble() * 100).toInt()
                        stepParsing.progress = progress
                        progressListener.onProgress(stepParsing, false)
                    }
                }
            }
        }

        return XlsDocumentEntity(
            title = strings[POSITION_TITLE],
            creationDate = strings[POSITION_CREATION_DATE],
            columnNames = strings.subList(
                POSITION_COLUMN_NAMES_START,
                POSITION_COLUMN_NAMES_END
            ),
            data = booksData.apply { forEach { it.key.booksCount = it.value.size } }
        ).also { progressListener.onProgress(ProgressStep.PARSING, true) }
    }

    private fun findValue(key: String, vararg strings: String): String? {
        if (key == KEY_DESCRIPTION) {
            val lastIndexOfKey = strings[1].lastIndexOfAny(KEYS_SET)
            val answer = if (lastIndexOfKey == -1) null
            else strings[1].substring(lastIndexOfKey)
            if (answer != null && answer.startsWith(KEY_ISBN)) {
                val matcher = Pattern.compile(REGEX_ISBN_NUMBER).matcher(answer)
                matcher.find()
                val isbnNumber = matcher.group()
                return answer.removePrefix(KEY_ISBN).removePrefix(isbnNumber)
            } else if (answer != null && answer.startsWith(KEY_COVER)) {
                val startIndex = KEY_COVER.length + FORMAT_LENGTH
                return if (answer.length > startIndex) answer.substring(startIndex) else null
            }
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
        val reader = BufferedReader(InputStreamReader(inputStream, ENCODING))
        val stringBuilder = StringBuilder()
        while (reader.readLine()?.let { stringBuilder.append(it).append("\n") } != null) {
        }
        reader.close()
        return stringBuilder.toString()
    }

    companion object {
        private const val REGEX_ISBN_NUMBER = "[0-9-]+"
        private const val REGEX_XLS_DATA =
            "(?s)(?<=<Data ss:Type=\"String\">|<Data ss:Type=\"Number\">).*?(?=<\\/Data>)|section_title_1"
        private const val REGEX_XLS_CDATA_START = "<![CDATA["
        private const val REGEX_XLS_CDATA_END = "]]>"
        private const val ENCODING = "windows-1251"
        private const val POSITION_TITLE = 1
        private const val POSITION_CREATION_DATE = 2
        private const val POSITION_COLUMN_NAMES_START = 3
        private const val POSITION_COLUMN_NAMES_END = 12
        private const val DATA_DELIMETER = "section_title_1"
        private const val OBJECT_FIELD_COUNT = 10
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
        private const val KEY_DESCRIPTION = "KEY_DESCRIPTION"
        private const val FORMAT_LENGTH = 3
        private val KEYS_SET =
            setOf(
                KEY_ISBN,
                KEY_AUTHOR,
                KEY_PUBLISHER,
                KEY_SERIES,
                KEY_FORMAT,
                KEY_YEAR,
                KEY_PAGES,
                KEY_COVER
            )
    }
}