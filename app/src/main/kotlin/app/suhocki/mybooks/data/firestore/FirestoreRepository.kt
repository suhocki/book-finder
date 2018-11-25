package app.suhocki.mybooks.data.firestore

import app.suhocki.mybooks.data.firestore.entity.FirestoreBanner
import app.suhocki.mybooks.data.firestore.entity.FirestoreCategory
import app.suhocki.mybooks.data.mapper.Mapper
import app.suhocki.mybooks.data.notification.NotificationHelper
import app.suhocki.mybooks.domain.model.Banner
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.model.ShopInfo
import app.suhocki.mybooks.domain.repository.BannersRepository
import app.suhocki.mybooks.domain.repository.BooksRepository
import app.suhocki.mybooks.domain.repository.CategoriesRepository
import app.suhocki.mybooks.domain.repository.InfoRepository
import app.suhocki.mybooks.ui.base.entity.UploadControlEntity
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject
import kotlin.math.roundToInt

class FirestoreRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val mapper: Mapper,
    private val notificationHelper: NotificationHelper,
    private val snapshots: MutableList<DocumentSnapshot>
) : BooksRepository, CategoriesRepository, InfoRepository, BannersRepository {

    override fun setBanners(banners: List<Banner>) {
        val currentCount = AtomicInteger(0)
        banners.map { mapper.map<FirestoreBanner>(it) }.forEach { banner ->
            firestore.collection(BANNERS)
                .document(banner.id)
                .set(banner)
                .addOnSuccessListener { currentCount.incrementAndGet() }
        }
        while (currentCount.get() < banners.size) {
        }
    }

    override fun getBanners(): List<Banner> {
        val banners = mutableListOf<Banner>()
        var notFinished = true

        firestore.collection(FirestoreRepository.BANNERS)
            .get()
            .addOnCompleteListener {
                if (it.result != null) {
                    banners.addAll(it.result!!.toObjects(FirestoreBanner::class.java))
                    notFinished = false
                }
            }

        while (notFinished) {
        }

        return banners
    }

    override fun addCategories(categories: List<Category>) {
        val currentCount = AtomicInteger(0)
        categories.forEach { category ->
            firestore.collection(CATEGORIES)
                .document(category.id)
                .set(category)
                .addOnSuccessListener { currentCount.incrementAndGet() }
        }
        while (currentCount.get() < categories.size) {
        }
    }

    override fun getCategories(offset: Int, limit: Int): List<Category> {
        var notFinished = true
        val categories = mutableListOf<Category>()

        firestore.collection(FirestoreRepository.CATEGORIES)
            .apply { snapshots.getOrNull(offset)?.let { startAfter(it) } }
            .limit(limit.toLong())
            .get()
            .addOnSuccessListener {
                snapshots.addAll(it.documents)
                categories.addAll(it.toObjects(FirestoreCategory::class.java))
                notFinished = false
            }

        while (notFinished) {
        }

        return categories
    }

    override fun addBooks(books: List<Book>, uploadControl: UploadControlEntity?) {
        val totalCount = books.size
        val currentCount = AtomicInteger(0)
        books.forEach { book ->
            firestore.collection(BOOKS)
                .document(book.id)
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

    override fun setShopInfo(shopInfo: ShopInfo) {
        val isUploaded = AtomicBoolean(false)
        firestore.collection(SHOP_INFO)
            .document(SHOP_INFO)
            .set(shopInfo)
            .addOnSuccessListener { isUploaded.set(true) }
        while (!isUploaded.get()) {
        }
    }

    companion object FirestoreCollections {
        const val BOOKS = "books"
        const val CATEGORIES = "categories"
        const val BANNERS = "banners"
        const val SHOP_INFO = "shopInfo"
    }
}