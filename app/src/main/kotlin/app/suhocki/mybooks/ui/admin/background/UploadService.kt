package app.suhocki.mybooks.ui.admin.background

import android.app.IntentService
import android.content.Intent
import android.os.Environment
import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.googledrive.RemoteFilesRepository
import app.suhocki.mybooks.data.localstorage.LocalFilesRepository
import app.suhocki.mybooks.data.mapper.Mapper
import app.suhocki.mybooks.data.notification.NotificationHelper
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.data.service.ServiceHandler
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.di.Room
import app.suhocki.mybooks.di.module.UploadServiceModule
import app.suhocki.mybooks.domain.model.*
import app.suhocki.mybooks.domain.model.admin.File
import app.suhocki.mybooks.domain.model.statistics.*
import app.suhocki.mybooks.domain.repository.*
import app.suhocki.mybooks.ui.admin.eventbus.UploadCompleteEvent
import app.suhocki.mybooks.ui.base.entity.UploadControlEntity
import app.suhocki.mybooks.ui.base.mpeventbus.MPEventBus
import app.suhocki.mybooks.ui.firestore.FirestoreService
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.notificationManager
import toothpick.Toothpick
import javax.inject.Inject

class UploadService : IntentService("UploadService"), AnkoLogger {

    @Inject
    @field:Room
    lateinit var infoRepository: InfoRepository

    @Inject
    @field:Room
    lateinit var booksRepository: BooksRepository

    @Inject
    @field:Room
    lateinit var categoriesRepository: CategoriesRepository

    @Inject
    @field:Room
    lateinit var bannersRepository: BannersRepository

    @Inject
    lateinit var remoteFilesRepository: RemoteFilesRepository
    @Inject
    lateinit var localFilesRepository: LocalFilesRepository
    @Inject
    lateinit var statisticRepository: StatisticsRepository
    @Inject
    lateinit var mapper: Mapper
    @Inject
    lateinit var serviceHandler: ServiceHandler
    @Inject
    lateinit var notificationHelper: NotificationHelper
    @Inject
    lateinit var resourceManager: ResourceManager
    @Inject
    lateinit var uploadControl: UploadControlEntity

    private lateinit var file: File
    private lateinit var strings: ArrayList<String>
    private lateinit var document: XlsDocument

    private val scope by lazy {
        Toothpick.openScopes(DI.APP_SCOPE, DI.UPLOAD_SERVICE).apply {
            installModules(
                UploadServiceModule(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).path)
            )
        }
    }

    override fun onCreate() {
        super.onCreate()
        Toothpick.inject(this, scope)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent.extras.getString(ARG_COMMAND) == UploadService.Command.CANCEL) {
            notificationManager.cancel(NotificationHelper.NOTIFICATION_ID)
            MPEventBus.getDefault().postToAll(UploadCompleteEvent(true))
            serviceHandler.killUploadServiceProcess()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onHandleIntent(intent: Intent) {
        file = intent.getSerializableExtra(ARG_FILE) as File
        uploadControl.fileName = file.name

        val taskIds = resourceManager
            .getStringArrayIdentifiers(R.array.database_upload_steps)

        taskIds.forEach {
            uploadControl.stepRes = it
            uploadControl.sendProgress(0, notificationHelper)
            notificationHelper.showProgressNotification(it, 0)
            tasks[it]!!.invoke()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Toothpick.closeScope(DI.UPLOAD_SERVICE)
    }

    private val tasks = mapOf(
        R.string.step_downloading to {
            if (getDownloadedFile(file.id) == null) {
                val bytes = downloadFile(file.id)
                saveFile(file.id, file.name, bytes)
            }
        },

        R.string.step_unzipping to {
            uploadControl.sendProgress(0, notificationHelper)

            if (getUnzippedFile(file.id) == null) {
                val zipped = getDownloadedFile(file.id)!!
                val unzipped = unzip(zipped)
                val fileName = LocalFilesRepository.UNZIPPED_FILE_PREFIX + unzipped.name
                saveFile(file.id, fileName, unzipped.readBytes())
            }
        },

        R.string.step_string_analysing to {
            val unzipped = getUnzippedFile(file.id)!!
            strings = parseXlsStructure(unzipped)
        },

        R.string.step_book_construct to {
            document = extractXlsDocument(strings)
        },

        R.string.step_saving_to_local to {
            saveBooksToLocal(document.booksData)
            saveStatisticsData(document.statisticsData)
            saveShopInfo(document.shopInfo)
            saveBannersData(document.bannersData)
        },

        R.string.step_saving_to_remote to {
            serviceHandler.startUpdateService(
                FirestoreService.Command.PUSH_DATABASE, uploadControl
            )
        }
    )

    private fun getDownloadedFile(fileId: String) =
        localFilesRepository.getDownloadedFile(fileId)

    private fun downloadFile(fileId: String) =
        remoteFilesRepository.downloadFile(fileId)

    private fun saveFile(folder: String, fileName: String, bytes: ByteArray) =
        localFilesRepository.saveFile(folder, fileName, bytes)

    private fun unzip(file: java.io.File) =
        localFilesRepository.unzip(file)

    private fun parseXlsStructure(xlsFile: java.io.File) =
        localFilesRepository.parseXlsStructure(xlsFile)

    private fun extractXlsDocument(strings: ArrayList<String>) =
        localFilesRepository.extractXlsDocument(strings)

    private fun saveBooksToLocal(data: Map<out Category, Collection<Book>>) {
        categoriesRepository.addCategories(data.keys.toList())
        booksRepository.addBooks(data.values.flatMap { books -> books }.toList())
    }

    private fun saveStatisticsData(
        statistics: Map<Category, Statistics>
    ) = with(statisticRepository) {
        setAuthorStatistics(mapper.map(statistics, AuthorStatistics::class.java))
        setPublisherStatistics(mapper.map(statistics, PublisherStatistics::class.java))
        setStatusStatistics(mapper.map(statistics, StatusStatistics::class.java))
        setYearStatistics(mapper.map(statistics, YearStatistics::class.java))
        setPriceStatistics(mapper.map(statistics, PriceStatistics::class.java))
    }

    private fun saveShopInfo(shopInfo: ShopInfo) =
        infoRepository.setShopInfo(shopInfo)

    private fun saveBannersData(bannersData: List<Banner>) {
        bannersRepository.setBanners(bannersData)
    }

    private fun getUnzippedFile(fileId: String): java.io.File? =
        localFilesRepository.getUnzippedFile(fileId)

    companion object {
        const val ARG_FILE = "ARG_FILE"
        const val PROGRESS_MAX = 100
        const val ARG_COMMAND = "ARG_COMMAND"
    }

    object Command {
        const val CANCEL = "CANCEL"
    }
}