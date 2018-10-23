package app.suhocki.mybooks.ui.base

import android.support.annotation.IntDef
import app.suhocki.mybooks.ui.main.MainActivity

@Retention
@IntDef(
    MainActivity.TabPositions.TAB_POSITION_CATALOG,
    MainActivity.TabPositions.TAB_POSITION_SEARCH,
    MainActivity.TabPositions.TAB_POSITION_INFO,
    MainActivity.TabPositions.TAB_POSITION_ADMIN
)
annotation class TabPosition