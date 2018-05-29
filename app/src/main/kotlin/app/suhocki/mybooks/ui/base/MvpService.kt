package app.suhocki.mybooks.ui.base

import android.app.Service
import com.arellomobile.mvp.MvpDelegate

abstract class MvpService : Service() {

    private lateinit var mvpDelegate: MvpDelegate<out MvpService>

    override fun onCreate() {
        super.onCreate()
        mvpDelegate = MvpDelegate(this).apply {
            onCreate()
            onAttach()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mvpDelegate.apply {
            onDetach()
            onDestroyView()
            onDestroy()
        }
    }
}