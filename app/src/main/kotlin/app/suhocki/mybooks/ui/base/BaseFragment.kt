package app.suhocki.mybooks.ui.base

import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatFragment
import org.jetbrains.anko.AnkoLogger


abstract class BaseFragment : MvpAppCompatFragment(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let { restoreState(it) }
    }

    open protected fun restoreState(state: Bundle) {}
}