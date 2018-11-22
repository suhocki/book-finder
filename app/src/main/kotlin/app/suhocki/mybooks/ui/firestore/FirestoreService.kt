package app.suhocki.mybooks.ui.firestore

import android.app.Service
import android.content.Intent
import android.support.annotation.StringDef
import app.suhocki.mybooks.data.firestore.FirestoreRepository
import app.suhocki.mybooks.data.firestore.entity.BannerEntity
import app.suhocki.mybooks.data.firestore.entity.CategoryEntity
import app.suhocki.mybooks.data.notification.NotificationHelper
import app.suhocki.mybooks.data.room.RoomRepository
import app.suhocki.mybooks.data.room.entity.BookEntity
import app.suhocki.mybooks.data.room.entity.ShopInfoEntity
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.di.ErrorReceiver
import app.suhocki.mybooks.domain.model.Banner
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.ui.admin.eventbus.UploadCompleteEvent
import app.suhocki.mybooks.ui.base.entity.UploadControlEntity
import app.suhocki.mybooks.ui.base.eventbus.BooksUpdatedEvent
import app.suhocki.mybooks.ui.base.eventbus.CatalogItemsUpdatedEvent
import app.suhocki.mybooks.ui.base.eventbus.ShopInfoUpdatedEvent
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.doAsync
import toothpick.Toothpick
import javax.inject.Inject

class FirestoreService : Service() {

    @Inject
    lateinit var firestore: FirebaseFirestore

    @Inject
    lateinit var roomRepository: RoomRepository

    @Inject
    lateinit var firestoreRepository: FirestoreRepository

    @Inject
    @field:ErrorReceiver
    lateinit var errorReceiver: (Throwable) -> Unit

    @Inject
    lateinit var notificationHelper: NotificationHelper

    private val firestoreCategories by lazy {
        firestore.collection(FirestoreRepository.CATEGORIES)
    }

    private val firestoreShopInfo by lazy {
        firestore.collection(FirestoreRepository.SHOP_INFO)
            .document(FirestoreRepository.SHOP_INFO)
    }

    private val firestoreBanners by lazy {
        firestore.collection(FirestoreRepository.BANNERS)
    }

    private lateinit var booksSnapshotListener: ListenerRegistration

    private fun getFirestoreBooks(categoryId: String) =
        firestore.collection(FirestoreRepository.BOOKS)
            .whereEqualTo(BookEntity.CATEGORY_ID, categoryId)

    private val scope by lazy { Toothpick.openScopes(DI.APP_SCOPE) }

    override fun onCreate() {
        super.onCreate()
        Toothpick.inject(this, scope)
    }

    override fun onBind(intent: Intent?) = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.extras.getString(ARG_COMMAND)) {
            Command.FETCH_CATALOG_ITEMS -> fetchCatalogItems()

            Command.FETCH_SHOP_INFO -> listenToShopInfo()

            Command.FETCH_BOOKS -> listenToBooks(intent.getStringExtra(ARG_CATEGORY_ID))

            Command.CANCEL_FETCH_BOOKS -> booksSnapshotListener.remove()

            Command.PUSH_DATABASE ->
                pushLocalDatabaseToFirestore(intent.getParcelableExtra(ARG_UPLOAD_CONTROL))
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun fetchCatalogItems() {
        Tasks.whenAllSuccess<Any>(
            firestoreBanners.get(),
            firestoreCategories.get()
        ).addOnSuccessListener { data ->
            doAsync(errorReceiver) {
                roomRepository.setBanners(data.asSequence().filterIsInstance<Banner>().toList())
                roomRepository.addCategories(data.asSequence().filterIsInstance<Category>().toList())

                listenToCatalogItems()
            }

        }
    }

    private fun listenToCatalogItems() {
        firestoreBanners.addSnapshotListener { snapshot, _ ->
            val banners = snapshot!!.toObjects(BannerEntity::class.java)
            doAsync {
                roomRepository.setBanners(banners)
                EventBus.getDefault().postSticky(CatalogItemsUpdatedEvent())
            }
        }
        firestoreCategories.addSnapshotListener { snapshot, _ ->
            val categories = snapshot!!.toObjects(CategoryEntity::class.java)
            doAsync {
                roomRepository.addCategories(categories)
                EventBus.getDefault().postSticky(CatalogItemsUpdatedEvent())
            }
        }
    }

    private fun listenToShopInfo() {
        firestoreShopInfo.addSnapshotListener { snapshot, _ ->
            val shopInfo = snapshot!!.toObject(ShopInfoEntity::class.java)!!
            doAsync {
                roomRepository.setShopInfo(shopInfo)
                EventBus.getDefault().postSticky(ShopInfoUpdatedEvent())
            }
        }
    }

    private fun listenToBooks(categoryId: String) {
        booksSnapshotListener = getFirestoreBooks(categoryId).addSnapshotListener { snapshot, _ ->
            val books =
                snapshot!!.toObjects(app.suhocki.mybooks.data.firestore.entity.BookEntity::class.java)
            doAsync {
                roomRepository.addBooks(books)
                EventBus.getDefault().postSticky(BooksUpdatedEvent())
            }
        }
    }

    private fun pushLocalDatabaseToFirestore(uploadControl: UploadControlEntity) {
        doAsync(errorReceiver) {

            with(firestoreRepository) {
                addBooks(roomRepository.getBooks(), uploadControl)
                addCategories(roomRepository.getCategories())
                setBanners(roomRepository.getBanners())
                setShopInfo(roomRepository.getShopInfo()!!)
            }

            EventBus.getDefault().post(UploadCompleteEvent(true))
            notificationHelper.showSuccessNotification(uploadControl.fileName)
        }
    }

    companion object {
        const val ARG_COMMAND = "ARG_COMMAND"
        const val ARG_UPLOAD_CONTROL = "ARG_UPLOAD_CONTROL"
        const val ARG_CATEGORY_ID = "ARG_CATEGORY_ID"
    }

    object Command {
        const val FETCH_CATALOG_ITEMS = "FETCH_CATALOG_ITEMS"
        const val FETCH_BOOKS = "FETCH_BOOKS"
        const val FETCH_SHOP_INFO = "FETCH_SHOP_INFO"
        const val CANCEL_FETCH_BOOKS = "CANCEL_FETCH_BOOKS"
        const val PUSH_DATABASE = "PUSH_DATABASE"
    }

    @Retention
    @StringDef(
        Command.FETCH_CATALOG_ITEMS,
        Command.PUSH_DATABASE
    )
    annotation class UpdateCommand
}