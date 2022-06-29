package kim.uno.mock.extension

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat

fun Window.enableLightStatusBar(enable: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        setSystemBarsAppearance(enable, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS)
    } else {
        setSystemBarsAppearance(enable, View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
    }
}

fun Window.enableLightNavigationBar(enable: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        setSystemBarsAppearance(enable, WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS)
    } else {
        setSystemBarsAppearance(enable, View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
    }
}

fun Activity.enableLightStatusBar(enable: Boolean) {
    window.enableLightStatusBar(enable)
}

fun Activity.enableLightNavigationBar(enable: Boolean) {
    window.enableLightNavigationBar(enable)
}

fun Activity.measureLightStatusBarRes(@ColorRes color: Int) {
    val color = ResourcesCompat.getColor(resources, color, null)
    measureLightStatusBar(color)
}

fun Activity.measureLightStatusBar(@ColorInt color: Int) {
    enableLightStatusBar(!color.isDark())
}

fun Activity.enableStatusBarTransparent(enable: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.setDecorFitsSystemWindows(!enable)
    } else {
        window.setSystemBarsAppearance(enable, View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }

    if (enable) {
        setStatusBarColor(Color.TRANSPARENT)
    }
}

fun Activity.setStatusBarColorRes(@ColorRes color: Int) {
    setStatusBarColor(getColor(color))
}

fun Activity.setStatusBarColor(@ColorInt color: Int) {
    window.statusBarColor = color
}

private fun Window.setSystemBarsAppearance(enable: Boolean, flag: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val insetsController = insetsController ?: return
        var flags = insetsController.systemBarsAppearance and flag.inv()
        if (enable) {
            flags = flags or flag
        }
        insetsController.setSystemBarsAppearance(
            flags, flag
        )
    } else {
        var flags = decorView.systemUiVisibility and flag.inv()
        if (enable) {
            flags = flags or flag
        }
        decorView.systemUiVisibility = flags
    }
}