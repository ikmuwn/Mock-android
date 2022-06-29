package kim.uno.mock.extension

import android.animation.LayoutTransition
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Path
import android.os.SystemClock
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import kotlin.math.min

var View.visible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

var View.visibleOrInvisible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.INVISIBLE
    }

@BindingAdapter("visible")
fun View.visible(visible: Boolean = true) {
    this.visible = visible
}

fun View.gone() {
    visible = false
}

fun View.invisible() {
    visibleOrInvisible = false
}

@BindingAdapter("visibleOrInvisible")
fun View.visibleOrInvisible(visible: Boolean) {
    visibleOrInvisible = visible
}

@BindingAdapter("visibleByText")
fun View.viewVisibleByText(text: String?) {
    visible = !text.isNullOrBlank()
}

@BindingAdapter("isSelected")
fun View.isSelected(selected: Boolean) {
    isSelected = selected
//    contentDescription = context.getString(
//        if (selected) R.string.content_description_selected
//        else R.string.content_description_unselected
//    )
}

fun View.setPadding(padding: Int) {
    setPadding(padding, padding, padding, padding)
}

var View.leftPadding: Int
    get() = paddingLeft
    set(value) {
        setPadding(value, paddingTop, paddingRight, paddingBottom)
    }

var View.topPadding: Int
    get() = paddingTop
    set(value) {
        setPadding(paddingLeft, value, paddingRight, paddingBottom)
    }

var View.rightPadding: Int
    get() = paddingRight
    set(value) {
        setPadding(paddingLeft, paddingTop, value, paddingBottom)
    }

var View.bottomPadding: Int
    get() = paddingBottom
    set(value) {
        setPadding(paddingLeft, paddingTop, paddingRight, value)
    }

fun View.getClipPath(
    radiusTopLeft: Int = 0,
    radiusTopRight: Int = 0,
    radiusBottomLeft: Int = 0,
    radiusBottomRight: Int = 0,
    cutoutTopLeft: Int = 0,
    cutoutTopRight: Int = 0,
    cutoutBottomLeft: Int = 0,
    cutoutBottomRight: Int = 0,
    cutoutLeftInset: Int = 0,
    cutoutTopInset: Int = 0,
    cutoutRightInset: Int = 0,
    cutoutBottomInset: Int = 0,
    inset: Float = 0f
) = makeClipPath(
    width = measuredWidth,
    height = measuredHeight,
    radiusTopLeft = radiusTopLeft,
    radiusTopRight = radiusTopRight,
    radiusBottomLeft = radiusBottomLeft,
    radiusBottomRight = radiusBottomRight,
    cutoutTopLeft = cutoutTopLeft,
    cutoutTopRight = cutoutTopRight,
    cutoutBottomLeft = cutoutBottomLeft,
    cutoutBottomRight = cutoutBottomRight,
    cutoutLeftInset = cutoutLeftInset,
    cutoutTopInset = cutoutTopInset,
    cutoutRightInset = cutoutRightInset,
    cutoutBottomInset = cutoutBottomInset,
    inset = inset
)

fun makeClipPath(
    width: Int,
    height: Int,
    radiusTopLeft: Int = 0,
    radiusTopRight: Int = 0,
    radiusBottomLeft: Int = 0,
    radiusBottomRight: Int = 0,
    cutoutTopLeft: Int = 0,
    cutoutTopRight: Int = 0,
    cutoutBottomLeft: Int = 0,
    cutoutBottomRight: Int = 0,
    cutoutLeftInset: Int = 0,
    cutoutTopInset: Int = 0,
    cutoutRightInset: Int = 0,
    cutoutBottomInset: Int = 0,
    inset: Float = 0f
): Path {

    val minWidth = min(width / 2, height / 2) - inset.toInt()
    val radiusTopLeft = min(radiusTopLeft, minWidth)
    val radiusTopRight = min(radiusTopRight, minWidth)
    val radiusBottomLeft = min(radiusBottomLeft, minWidth)
    val radiusBottomRight = min(radiusBottomRight, minWidth)
    val cutoutTopLeft = min(cutoutTopLeft, minWidth)
    val cutoutTopRight = min(cutoutTopRight, minWidth)
    val cutoutBottomLeft = min(cutoutBottomLeft, minWidth)
    val cutoutBottomRight = min(cutoutBottomRight, minWidth)

    val clipPath = Path()

    if (cutoutTopLeft > 0) {
        clipPath.moveTo(inset + cutoutTopLeft + cutoutTopInset, inset)
    } else {
        clipPath.moveTo(inset + radiusTopLeft, inset)
    }

    when {
        cutoutTopRight > 0 -> {
            clipPath.lineTo(width - cutoutTopRight - inset - cutoutTopInset, inset)
            clipPath.lineTo(width - inset, inset + cutoutTopRight + cutoutRightInset)
        }
        radiusTopRight > 0 -> {
            clipPath.lineTo(width - radiusTopRight - inset, inset)
            clipPath.cubicTo(
                width - radiusTopRight - inset, inset,
                width - inset, inset,
                width - inset, inset + radiusTopRight
            )
        }
        else -> clipPath.lineTo(width - inset, inset)
    }

    when {
        cutoutBottomRight > 0 -> {
            clipPath.lineTo(width - inset, height - cutoutBottomRight - inset - cutoutRightInset)
            clipPath.lineTo(width - cutoutBottomRight - inset - cutoutBottomInset, height - inset)
        }
        radiusBottomRight > 0 -> {
            clipPath.lineTo(width - inset, height - radiusBottomRight - inset)
            clipPath.cubicTo(
                width - inset, height - radiusBottomRight - inset,
                width - inset, height - inset,
                width - radiusBottomRight - inset, height - inset
            )
        }
        else -> clipPath.lineTo(width - inset, height - inset)
    }

    when {
        cutoutBottomLeft > 0 -> {
            clipPath.lineTo(inset + cutoutBottomLeft + cutoutBottomInset, height - inset)
            clipPath.lineTo(inset, height - cutoutBottomLeft - inset - cutoutLeftInset)
        }
        radiusBottomLeft > 0 -> {
            clipPath.lineTo(inset + radiusBottomLeft, height - inset)
            clipPath.cubicTo(
                inset + radiusBottomLeft, height - inset,
                inset, height - inset,
                inset, height - radiusBottomLeft - inset
            )
        }
        else -> clipPath.lineTo(inset, height - inset)
    }

    when {
        cutoutTopLeft > 0 -> {
            clipPath.lineTo(inset, inset + cutoutTopLeft + cutoutLeftInset)
            clipPath.lineTo(inset + cutoutTopLeft + cutoutTopInset, inset)
        }
        radiusTopLeft > 0 -> {
            clipPath.lineTo(inset, inset + radiusTopLeft)
            clipPath.cubicTo(
                inset, inset + radiusTopLeft,
                inset, inset,
                inset + radiusTopLeft, inset
            )
        }
        else -> clipPath.lineTo(inset, inset)
    }

    clipPath.close()
    return clipPath
}

fun Int.toPixel() = (Resources.getSystem().displayMetrics.density * this).toInt()
fun Float.toPixel() = Resources.getSystem().displayMetrics.density * this

fun View.setOnClickThrottleListener(throttle: Long = 300L, action: (View) -> Unit) {
    val throttleAction = ThrottleAction(this, throttle, action)
    setOnClickListener {
        instantFocusInTouchMode()
        throttleAction.action()
    }
}

fun View.instantFocusInTouchMode() {
    val focusableInTouchMode = isFocusableInTouchMode
    isFocusableInTouchMode = true
    requestFocus()
    isFocusableInTouchMode = focusableInTouchMode
    clearFocus()
}

private class ThrottleAction(
    private val view: View,
    private val throttle: Long,
    private val action: (View) -> Unit
) {

    private var lastActionTime = 0L

    fun action() {
        val now = SystemClock.elapsedRealtime()
        val millisecondsPassed = now - lastActionTime
        val actionAllowed = millisecondsPassed > throttle
        lastActionTime = now

        if (actionAllowed)
            action.invoke(view)
    }
}

fun ViewGroup.notifyLayoutChanged() {
    layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
}

fun Int.isDark(): Boolean {
    if (this == Color.TRANSPARENT) {
        return false
    }

    val r = Color.red(this)
    val g = Color.green(this)
    val b = Color.blue(this)
    return (r * 299 + g * 587 + b * 114) / 1000 < 200
}

fun View.measuredWidth(measuredHeight: Int): Int {
    val widthMeasureSpec =
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    val heightMeasureSpec =
        View.MeasureSpec.makeMeasureSpec(measuredHeight, View.MeasureSpec.AT_MOST)
    measure(widthMeasureSpec, heightMeasureSpec)
    return measuredWidth
}

fun View.measuredHeight(measuredWidth: Int): Int {
    val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(
        measuredWidth,
        View.MeasureSpec.AT_MOST
    )
    val heightMeasureSpec =
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    measure(widthMeasureSpec, heightMeasureSpec)
    return measuredHeight
}