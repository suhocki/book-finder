package app.suhocki.mybooks.ui.firestore

import android.app.Service
import android.content.Intent
import android.support.annotation.StringDef
import app.suhocki.mybooks.data.firestore.FirestoreRepository
import app.suhocki.mybooks.data.firestore.entity.CategoryEntity
import app.suhocki.mybooks.data.notification.NotificationHelper
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.di.ErrorReceiver
import app.suhocki.mybooks.di.Firestore
import app.suhocki.mybooks.di.Room
import app.suhocki.mybooks.domain.repository.BooksRepository
import app.suhocki.mybooks.ui.admin.eventbus.UploadCompleteEvent
import app.suhocki.mybooks.ui.base.entity.UploadControlEntity
import app.suhocki.mybooks.ui.base.eventbus.CategoriesUpdatedEvent
import com.google.firebase.firestore.FirebaseFirestore
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

    override fun onCreate() {
        super.onCreate()
        Toothpick.inject(this, Toothpick.openScopes(DI.APP_SCOPE))
    }

    override fun onBind(intent: Intent?) = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.extras.getString(ARG_COMMAND)) {
            Command.PULL_CATEGORIES -> listenToCategoriesUpdates()

            Command.PUSH_DATABASE -> {
                pushLocalDatabaseToFirestore(intent.getParcelableExtra(ARG_UPLOAD_CONTROL))
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun listenToCategoriesUpdates() {
        firestoreCategories.addSnapshotListener { snapshot, _ ->
            val categories = snapshot!!.toObjects(CategoryEntity::class.java)
            doAsync {
                localBooksRepository.setCategories(categories.toSet())
                EventBus.getDefault().post(CategoriesUpdatedEvent())
            }
        }
    }

    private fun pushLocalDatabaseToFirestore(uploadControl: UploadControlEntity) {
        doAsync(errorReceiver) {
            val books = localBooksRepository.getBooks()
            val categories = localBooksRepository.getCategories()

            remoteBooksRepository.setBooks(books, uploadControl)
            remoteBooksRepository.setCategories(categories.toSet())

            EventBus.getDefault().post(UploadCompleteEvent(true))
            notificationHelper.showSuccessNotification(uploadControl.fileName)
        }
    }

    companion object {
        const val ARG_COMMAND = "ARG_COMMAND"
        const val ARG_UPLOAD_CONTROL = "ARG_UPLOAD_CONTROL"
    }

    object Command {
        const val PULL_CATEGORIES = "PULL_CATEGORIES"
        const val PUSH_DATABASE = "PUSH_DATABASE"
    }

    @Retention
    @StringDef(
        Command.PULL_CATEGORIES,
        Command.PUSH_DATABASE
    )
    annotation class UpdateCommand
}