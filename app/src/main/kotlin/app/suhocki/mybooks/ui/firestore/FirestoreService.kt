package app.suhocki.mybooks.ui.firestore

import android.app.Service
import android.content.Intent
import android.support.annotation.StringDef
import app.suhocki.mybooks.data.firestore.FirestoreRepository
import app.suhocki.mybooks.data.firestore.entity.FirestoreBook
import app.suhocki.mybooks.data.firestore.entity.FirestoreCategory
import app.suhocki.mybooks.data.mapper.Mapper
import app.suhocki.mybooks.data.notification.NotificationHelper
import app.suhocki.mybooks.data.room.RoomRepository
import app.suhocki.mybooks.data.room.entity.BookDbo
import app.suhocki.mybooks.data.room.entity.ShopInfoDbo
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.di.ErrorReceiver
import app.suhocki.mybooks.domain.repository.SettingsRepository
import app.suhocki.mybooks.ui.admin.eventbus.UploadCompleteEvent
import app.suhocki.mybooks.ui.base.entity.UploadControlEntity
import app.suhocki.mybooks.ui.base.eventbus.BooksUpdatedEvent
import app.suhocki.mybooks.ui.base.eventbus.ShopInfoUpdatedEvent
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.doAsync
import toothpick.Toothpick
import javax.inject.Inject

class FirestoreService : Service() {

    @Inject
    @field:ErrorReceiver
    lateinit var errorReceiver: (Throwable) -> Unit

    @Inject
    lateinit var firestore: FirebaseFirestore
    @Inject
    lateinit var roomRepository: RoomRepository
    @Inject
    lateinit var firestoreRepository: FirestoreRepository
    @Inject
    lateinit var notificationHelper: NotificationHelper
    @Inject
    lateinit var settingsRepository: SettingsRepository
    @Inject
    lateinit var mapper: Mapper

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
            .whereEqualTo(BookDbo.CATEGORY_ID, categoryId)

    private val scope by lazy { Toothpick.openScopes(DI.APP_SCOPE) }

    override fun onCreate() {
        super.onCreate()
        Toothpick.inject(this, scope)
    }

    override fun onBind(intent: Intent?) = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.extras.getString(ARG_COMMAND)) {
            Command.LISTEN_CATEGORIES -> listenCategories()

            Command.FETCH_SHOP_INFO -> listenToShopInfo()

            Command.FETCH_BOOKS -> listenToBooks(intent.getStringExtra(ARG_CATEGORY_ID))

            Command.CANCEL_FETCH_BOOKS -> booksSnapshotListener.remove()

            Command.PUSH_CHANGES -> pushChanges(intent.getParcelableExtra(ARG_UPLOAD_CONTROL))
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun listenCategories() {
        firestore.collection(FirestoreRepository.CATEGORIES)
            .limit(20)
            .addSnapshotListener { snapshot, _ ->
                val categories = snapshot!!.toObjects(FirestoreCategory::class.java)
                doAsync {
                    roomRepository.addCategories(categories)
                }
            }
    }

    private fun listenToShopInfo() {
        firestoreShopInfo.addSnapshotListener { snapshot, _ ->
            val shopInfo = snapshot!!.toObject(ShopInfoDbo::class.java)!!
            doAsync {
                roomRepository.setShopInfo(shopInfo)
                EventBus.getDefault().postSticky(ShopInfoUpdatedEvent())
            }
        }
    }

    private fun listenToBooks(categoryId: String) {
        booksSnapshotListener = getFirestoreBooks(categoryId).addSnapshotListener { snapshot, _ ->
            val books = snapshot!!.toObjects(FirestoreBook::class.java)
            doAsync {
                roomRepository.addBooks(books)
                EventBus.getDefault().postSticky(BooksUpdatedEvent())
            }
        }
    }

    private fun pushChanges(
        uploadControl: UploadControlEntity
    ) = doAsync(errorReceiver) {
        val updateDate = settingsRepository.updateDate
        val categories = roomRepository.getCategories(updateDate)
            .map { mapper.map<FirestoreCategory>(it) }
        val books = roomRepository.getBooks(updateDate)
            .map { mapper.map<FirestoreBook>(it) }

        with(firestoreRepository) {
            addBooks(books, uploadControl)
            addCategories(categories)
            setBanners(roomRepository.getBanners())
            setShopInfo(roomRepository.getShopInfo()!!)
        }

        EventBus.getDefault().post(UploadCompleteEvent(true))
        notificationHelper.showSuccessNotification(uploadControl.fileName)
    }

    companion object {
        const val ARG_COMMAND = "ARG_COMMAND"
        const val ARG_UPLOAD_CONTROL = "ARG_UPLOAD_CONTROL"
        const val ARG_CATEGORY_ID = "ARG_CATEGORY_ID"
    }

    object Command {
        const val LISTEN_CATEGORIES = "LISTEN_CATEGORIES"
        const val FETCH_BOOKS = "FETCH_BOOKS"
        const val FETCH_SHOP_INFO = "FETCH_SHOP_INFO"
        const val CANCEL_FETCH_BOOKS = "CANCEL_FETCH_BOOKS"
        const val PUSH_CHANGES = "PUSH_CHANGES"
    }

    @Retention
    @StringDef(
        Command.LISTEN_CATEGORIES,
        Command.PUSH_CHANGES
    )
    annotation class UpdateCommand
}