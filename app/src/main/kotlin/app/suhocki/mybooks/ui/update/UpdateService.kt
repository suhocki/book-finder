package app.suhocki.mybooks.ui.update

import android.app.Service
import android.content.Intent
import app.suhocki.mybooks.data.firestore.FirestoreRepository
import app.suhocki.mybooks.data.room.RoomRepository
import app.suhocki.mybooks.data.room.entity.CategoryEntity
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.di.Room
import app.suhocki.mybooks.ui.base.eventbus.CategoriesUpdatedEvent
import com.google.firebase.firestore.FirebaseFirestore
import org.greenrobot.eventbus.EventBus
import toothpick.Toothpick
import javax.inject.Inject

class UpdateService : Service() {

    @Inject
    lateinit var firebaseFirestore: FirebaseFirestore

    @Inject
    @Room
    lateinit var booksRepository: RoomRepository

    private val categories by lazy {
        firebaseFirestore.collection(FirestoreRepository.CATEGORIES)
    }

    override fun onCreate() {
        super.onCreate()
        Toothpick.inject(this, Toothpick.openScopes(DI.APP_SCOPE))
    }

    override fun onBind(intent: Intent?) = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.extras.getString(ARG_COMMAND)) {
            Command.UPDATE_CATEGORIES -> listenToCategoriesUpdates()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun listenToCategoriesUpdates() {
        categories.addSnapshotListener { snapshot, _ ->
            val categories = snapshot!!.toObjects(CategoryEntity::class.java)
            booksRepository.setCategories(categories.toSet())
            EventBus.getDefault().post(CategoriesUpdatedEvent())
        }
    }

    companion object {
        const val ARG_COMMAND = "ARG_COMMAND"
    }

    object Command {
        const val UPDATE_CATEGORIES = "UPDATE_CATEGORIES"
    }
}