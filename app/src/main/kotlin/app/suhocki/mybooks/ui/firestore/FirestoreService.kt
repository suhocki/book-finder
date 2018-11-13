package app.suhocki.mybooks.ui.firestore

import android.app.Service
import android.content.Intent
import android.support.annotation.StringDef
import app.suhocki.mybooks.data.firestore.FirestoreRepository
import app.suhocki.mybooks.data.notification.NotificationHelper
import app.suhocki.mybooks.data.room.entity.BookEntity
import app.suhocki.mybooks.data.room.entity.CategoryEntity
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.di.ErrorReceiver
import app.suhocki.mybooks.di.Firestore
import app.suhocki.mybooks.di.Room
import app.suhocki.mybooks.domain.repository.BooksRepository
import app.suhocki.mybooks.ui.admin.eventbus.UploadCompleteEvent
import app.suhocki.mybooks.ui.base.entity.UploadControlEntity
import app.suhocki.mybooks.ui.base.eventbus.BooksUpdatedEvent
import app.suhocki.mybooks.ui.base.eventbus.CategoriesUpdatedEvent
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
    @field:ErrorReceiver
    lateinit var errorReceiver: (Throwable) -> Unit

    @Inject
    lateinit var notificationHelper: NotificationHelper

    private val firestoreCategories by lazy {
        firestore.collection(FirestoreRepository.CATEGORIES)
    }

    private lateinit var booksSnapshotListener: ListenerRegistration

    private fun getFirestoreBooks(categoryId: String) =
        firestore.collection(FirestoreRepository.BOOKS)
            .whereEqualTo(BookEntity.FIELD_CATEGORY, categoryId)

    override fun onCreate() {
        super.onCreate()
        Toothpick.inject(this, Toothpick.openScopes(DI.APP_SCOPE))
    }

    override fun onBind(intent: Intent?) = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.extras.getString(ARG_COMMAND)) {
            Command.PULL_CATEGORIES -> listenToCategoriesUpdates()

            Command.PULL_BOOKS ->
                listenToBooksUpdates(intent.getStringExtra(ARG_CATEGORY_ID))

            Command.CANCEL_PULL_BOOKS ->
                booksSnapshotListener.remove()

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

            remoteBooksRepository.addBooks(books, uploadControl)
            remoteBooksRepository.addCategories(categories)

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