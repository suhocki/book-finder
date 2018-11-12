package app.suhocki.mybooks.data.firestore

import android.arch.persistence.db.SupportSQLiteQuery
import app.suhocki.mybooks.data.notification.NotificationHelper
import app.suhocki.mybooks.data.room.entity.BookEntity
import app.suhocki.mybooks.data.room.entity.CategoryEntity
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.repository.BooksRepository
import app.suhocki.mybooks.ui.base.entity.UploadControlEntity
import com.google.firebase.firestore.FirebaseFirestore
import org.jetbrains.anko.AnkoLogger
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject
import kotlin.math.roundToInt

class FirestoreRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val notificationHelper: NotificationHelper
) : BooksRepository, AnkoLogger {

    override fun getCategories(): List<Category> {
        val isFinished = AtomicBoolean(false)
        val result = mutableListOf<Category>()
        firebaseFirestore.collection(CATEGORIES)
            .get()
            .addOnSuccessListener {
                result.addAll(it.toObjects(Category::class.java))
                isFinished.set(true)
            }
        while (!isFinished.get()) {
        }
        return result
    }

    override fun setCategories(categories: List<Category>) {
        val totalCount = categories.size
        val currentCount = AtomicInteger(0)
        categories.forEach { category ->
            firebaseFirestore.collection(CATEGORIES)
                .document((category as CategoryEntity).id)
                .set(category)
                .addOnSuccessListener { currentCount.incrementAndGet() }
        }
        while (currentCount.get() < totalCount) {
        }
    }

    override fun getBooks(): List<Book> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setBooks(books: List<Book>, uploadControl: UploadControlEntity?) {
        val totalCount = books.size
        val currentCount = AtomicInteger(0)
        books.forEach { book ->
            firebaseFirestore.collection(BOOKS)
                .document(book.productCode)
                .set(book)
                .addOnSuccessListener {
                    currentCount.incrementAndGet()
                    val progress = (currentCount.toDouble() / totalCount * 100).roundToInt()
                    uploadControl!!.sendProgress(progress, notificationHelper)
                }
        }
        while (currentCount.get() < totalCount) {
        }
    }

    override fun getBooksFor(category: Category): List<BookEntity> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun search(text: String): List<BookEntity> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun filter(query: SupportSQLiteQuery): List<BookEntity> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    companion object FirestoreCollections {
        const val BOOKS = "books"
        const val CATEGORIES = "categories"
    }
}