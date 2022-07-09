package kim.uno.mock.extension

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

private var toast: Toast? = null

fun Context.showToast(@StringRes message: Int, duration: Int = Toast.LENGTH_SHORT) {
    showToast(getString(message), duration)
}

fun Context.showToast(message: String?, duration: Int = Toast.LENGTH_SHORT) {
    message ?: return
    toast?.cancel()
    toast = makeToast(message, duration).also { it.show() }
}

fun Fragment.showToast(@StringRes message: Int, duration: Int = Toast.LENGTH_SHORT) {
    context?.showToast(message, duration)
}

fun Fragment.showToast(message: String?, duration: Int = Toast.LENGTH_SHORT) {
    context?.showToast(message, duration)
}

private fun Context.makeToast(message: String?, duration: Int): Toast {
    return Toast.makeText(this, message, duration)
}
