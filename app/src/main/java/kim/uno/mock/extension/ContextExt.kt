package kim.uno.mock.extension

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import kotlin.math.max

val Context.screenWidth: Int
    get() = applicationContext.resources.displayMetrics.widthPixels

val Context.screenHeight: Int
    get() = applicationContext.resources.displayMetrics.heightPixels

val Context.realDisplayPoint: Point?
    get() = try {
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val bounds = windowManager.maximumWindowMetrics.bounds
            Point(bounds.width(), bounds.height())
        } else {
            Point().apply { windowManager.defaultDisplay.getRealSize(this) }
        }
    } catch (e: Exception) {
        null
    }

val Context.realDisplayWidth: Int
    get() = realDisplayPoint?.x ?: screenWidth

val Context.realDisplayHeight: Int
    get() = realDisplayPoint?.y ?: screenHeight

val Context.statusBarHeight: Int
    get() = if (!isPopUpView) {
        statusBarHeightDimension
    } else {
        0
    }

private val Context.statusBarHeightDimension: Int
    get() = try {
        val redId = resources.getIdentifier("status_bar_height", "dimen", "android")
        val dimension = resources.getDimension(redId).toInt()
        if (this is Activity) {
            val rect = Rect().apply { window.decorView.rootView.getWindowVisibleDisplayFrame(this) }
            max(rect.top, dimension)
        } else {
            dimension
        }
    } catch (e: Throwable) {
        0
    }

val Context.navigationBarHeight: Float
    get() = if (!isPopUpView) {
        navigationBarHeightDimension
    } else {
        0f
    }

private val Context.navigationBarHeightDimension: Float
    get() = try {
        if (realDisplayHeight > statusBarHeightDimension + screenHeight) {
            val resId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
            resources.getDimension(resId)
        } else {
            0f
        }
    } catch (e: Throwable) {
        0f
    }

/**
 * 실제 화면보다 dimen 합이 작은 경우 팝업 윈도우로 판단
 * navigation bar height float <> int 유실 보정하여 계산
 */
val Context.isPopUpView: Boolean
    get() = realDisplayHeight > statusBarHeightDimension + screenHeight + navigationBarHeightDimension + 10

val Context.isAlive: Boolean
    get() = this !is Activity || !(isFinishing || isDestroyed)

val Context.supportFragmentManager: FragmentManager?
    get() = fragmentActivity?.supportFragmentManager

val Context.fragmentActivity: FragmentActivity?
    get() {
        if (this is FragmentActivity && isAlive) {
            return this
        } else if (this is ContextWrapper) {
            return baseContext.fragmentActivity
        }
        return null
    }