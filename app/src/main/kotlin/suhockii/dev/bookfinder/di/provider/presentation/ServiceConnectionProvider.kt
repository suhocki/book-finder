package suhockii.dev.bookfinder.di.provider.presentation

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import javax.inject.Inject
import javax.inject.Provider

class ServiceConnectionProvider @Inject constructor() :
    Provider<ServiceConnection>, AnkoLogger {

    override fun get(): ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            info { "onServiceConnected" }
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            info { "onServiceDisconnected" }
        }
    }
}