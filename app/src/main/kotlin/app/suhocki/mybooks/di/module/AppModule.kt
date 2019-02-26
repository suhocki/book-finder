package app.suhocki.mybooks.di.module

import android.content.Context
import android.os.Build
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.di.Converters
import app.suhocki.mybooks.di.ErrorReceiver
import app.suhocki.mybooks.di.provider.ErrorReceiverProvider
import app.suhocki.mybooks.di.provider.MapperConvertersProvider
import app.suhocki.mybooks.model.system.message.SystemMessageNotifier
import app.suhocki.mybooks.ui.base.entity.UploadControlEntity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import net.grandcentrix.tray.AppPreferences
import org.jetbrains.anko.configuration
import toothpick.config.Module
import java.util.*

class AppModule(context: Context) : Module() {

    private val locale =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            context.configuration.locales.get(0)
        else context.configuration.locale

    private val firebaseFirestore =
        FirebaseFirestore.getInstance().apply {
            firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .setPersistenceEnabled(false)
                .build()
        }

    init {
        //Global dependencies
        bind(Context::class.java).toInstance(context)
        bind(ResourceManager::class.java).singletonInScope()
        bind(Locale::class.java).toInstance(locale)
        bind(AppPreferences::class.java).toInstance(AppPreferences(context))
        bind(Function1::class.java).withName(ErrorReceiver::class.java).toProvider(ErrorReceiverProvider::class.java).singletonInScope()
        bind(SystemMessageNotifier::class.java).toInstance(SystemMessageNotifier())

        //Mapper dependencies
        bind(Set::class.java).withName(Converters::class.java)
            .toProvider(MapperConvertersProvider::class.java).providesSingletonInScope()

        bind(UploadControlEntity::class.java).toInstance(UploadControlEntity())

        //Firestore
        bind(FirebaseFirestore::class.java).toInstance(firebaseFirestore)
    }
}