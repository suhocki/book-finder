package app.suhocki.mybooks

import app.suhocki.mybooks.domain.model.Book
import com.crashlytics.android.answers.AddToCartEvent
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent
import java.math.BigDecimal
import java.util.*

object Analytics {
    private const val CURRENCY_CODE_BYR = "BYR"

    fun bookAddedToCart(book: Book) {
        if (BuildConfig.DEBUG) return
        val toCartEvent = AddToCartEvent()
            .putItemPrice(BigDecimal.valueOf(book.price))
            .putCurrency(Currency.getInstance(CURRENCY_CODE_BYR))
            .putItemName(book.shortName)
            .putItemType(book.website)
            .putItemId(book.id)
        Answers.getInstance().logAddToCart(toCartEvent)
    }

    fun custom(message: String) {
        if (BuildConfig.DEBUG) return
        Answers.getInstance().logCustom(CustomEvent(message))
    }
}