package app.suhocki.mybooks.ui.firestore

import android.app.Service
import android.content.Intent
import android.support.annotation.StringDef
import app.suhocki.mybooks.data.firestore.FirestoreRepository
import app.suhocki.mybooks.data.notification.NotificationHelper
import app.suhocki.mybooks.data.room.entity.BookEntity
import app.suhocki.mybooks.data.room.entity.CategoryEntity
import app.suhocki.mybooks.data.room.entity.ShopInfoEntity
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.di.ErrorReceiver
import app.suhocki.mybooks.di.Firestore
import app.suhocki.mybooks.di.Room
import app.suhocki.mybooks.domain.repository.BooksRepository
import app.suhocki.mybooks.domain.repository.InfoRepository
import app.suhocki.mybooks.ui.admin.eventbus.UploadCompleteEvent
import app.suhocki.mybooks.ui.base.entity.UploadControlEntity
import app.suhocki.mybooks.ui.base.eventbus.BooksUpdatedEvent
import app.suhocki.mybooks.ui.base.eventbus.CategoriesUpdatedEvent
import app.suhocki.mybooks.ui.base.eventbus.ShopInfoUpdatedEvent
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
    @field:Firestore
    lateinit var remoteBooksRepository: BooksRepository

    @Inject
    @field:Room
    lateinit var localBooksRepository: BooksRepository

    @Inject
    @field:Firestore
    lateinit var remoteInfoRepository: InfoRepository

    @Inject
    @field:Room
    lateinit var localInfoRepository: InfoRepository

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

    private lateinit var booksSnapshotListener: ListenerRegistration

    private fun getFirestoreBooks(categoryId: String) =
        firestore.collection(FirestoreRepository.BOOKS)
            .whereEqualTo(BookEntity.FIELD_CATEGORY, categoryId)

    private val scope by lazy { Toothpick.openScopes(DI.APP_SCOPE) }

    override fun onCreate() {
        super.onCreate()
        Toothpick.inject(this, scope)
    }

    override fun onBind(intent: Intent?) = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.extras.getString(ARG_COMMAND)) {
            Command.PULL_CATEGORIES -> listenToCategoriesUpdates()

            Command.PULL_SHOP_INFO -> listenToShopInfoUpdates()

            Command.PULL_BOOKS -> listenToBooksUpdates(intent.getStringExtra(ARG_CATEGORY_ID))

            Command.CANCEL_PULL_BOOKS -> booksSnapshotListener.remove()

            Command.PUSH_DATABASE ->
                pushLocalDatabaseToFirestore(intent.getParcelableExtra(ARG_UPLOAD_CONTROL))
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun listenToCategoriesUpdates() {
        firestoreCategories.addSnapshotListener { snapshot, _ ->
            val categories = snapshot!!.toObjects(CategoryEntity::class.java)
            doAsync {
                localBooksRepository.addCategories(categories)
                EventBus.getDefault().postSticky(CategoriesUpdatedEvent())
            }
        }
    }

    private fun listenToShopInfoUpdates() {
        firestoreShopInfo.addSnapshotListener { snapshot, _ ->
            val shopInfo = snapshot!!.toObject(ShopInfoEntity::class.java)!!
            doAsync {
                localInfoRepository.setShopInfo(shopInfo)
                EventBus.getDefault().postSticky(ShopInfoUpdatedEvent())
            }
        }
    }

    private fun listenToBooksUpdates(categoryId: String) {
        booksSnapshotListener = getFirestoreBooks(categoryId).addSnapshotListener { snapshot, _ ->
            val books = snapshot!!.toObjects(BookEntity::class.java)
            doAsync {
                localBooksRepository.addBooks(books)
                EventBus.getDefault().postSticky(BooksUpdatedEvent())
            }
        }
    }

    private fun pushLocalDatabaseToFirestore(uploadControl: UploadControlEntity) {
        doAsync(errorReceiver) {
            val books = localBooksRepository.getBooks()
            val categories = localBooksRepository.getCategories()
            val shopInfo = localInfoRepository.getShopInfo()!!

            remoteBooksRepository.addBooks(books, uploadControl)
            remoteBooksRepository.addCategories(categories)
            remoteInfoRepository.setShopInfo(shopInfo)

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
        const val PULL_CATEGORIES = "PULL_CATEGORIES"
        const val PULL_BOOKS = "PULL_BOOKS"
        const val PULL_SHOP_INFO = "PULL_SHOP_INFO"
        const val CANCEL_PULL_BOOKS = "CANCEL_PULL_BOOKS"
        const val PUSH_DATABASE = "PUSH_DATABASE"
    }

    @Retention
    @StringDef(
        Command.PULL_CATEGORIES,
        Command.PUSH_DATABASE
    )
    annotation class UpdateCommand
}