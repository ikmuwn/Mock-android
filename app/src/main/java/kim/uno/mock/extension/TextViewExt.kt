package kim.uno.mock.extension

import android.content.Context
import android.graphics.Paint
import android.text.Html
import android.text.Selection
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.util.Linkify
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter

fun EditText.showKeyboard() {
    requestFocus()
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun View.hideKeyboard() {
    clearFocus()
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
}

@BindingAdapter("strikeThrough")
fun TextView.setStrikeThrough(strikeThrough: Boolean) {
    paintFlags = if (strikeThrough) {
        paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    } else {
        paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }
}

@BindingAdapter("htmlRes")
fun TextView.setHtml(@StringRes resId: Int) {
    setHtml(context.getString(resId))
}

@BindingAdapter("html")
fun TextView.setHtml(html: String?) {
    text = html?.toHtml()
}

fun String.toHtml(): CharSequence? {
    val matchResult = "(<([^>]+)>)".toRegex().find(this)
    return if (matchResult != null) {
        Html.fromHtml(replace("\n".toRegex(), "<br/>"), Html.FROM_HTML_MODE_LEGACY)
    } else {
        this
    }
}

@BindingAdapter("enableAutoLink")
fun TextView.enableAutoLink(enable: Boolean? = true) {
    enableAutoLink(enable = enable, callback = null)
}

fun TextView.enableAutoLink(enable: Boolean? = true, callback: (() -> Unit?)?) {
    if (enable == true) {
        autoLinkMask = Linkify.ALL
        linksClickable = false
        movementMethod = object : LinkMovementMethod() {
            override fun onTouchEvent(
                widget: TextView?,
                buffer: Spannable?,
                event: MotionEvent?
            ): Boolean {
                val action = event?.action
                if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
                    var x = event.x.toInt()
                    var y = event.y.toInt()
                    x -= widget!!.totalPaddingLeft
                    y -= widget.totalPaddingTop
                    x += widget.scrollX
                    y += widget.scrollY
                    val layout = widget.layout
                    val line = layout.getLineForVertical(y)
                    val off = layout.getOffsetForHorizontal(line, x.toFloat())
                    val links = buffer!!.getSpans(off, off, ClickableSpan::class.java)
                    if (links.isNotEmpty()) {
                        val link = links[0]
                        if (action == MotionEvent.ACTION_UP) {
//                            if (link is URLSpan && link.url.startsWith("http", true)) {
//                                callback?.invoke()
//                                val uri = context.getString(R.string.deeplink_web_format, link.url)
//                                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
//                            } else {
                            callback?.invoke()
                            link.onClick(widget)
//                            }
                        } else if (action == MotionEvent.ACTION_DOWN) {
                            Selection.setSelection(
                                buffer,
                                buffer.getSpanStart(link),
                                buffer.getSpanEnd(link)
                            )
                        }
                        return true
                    } else {
                        Selection.removeSelection(buffer)
                    }
                }

                return super.onTouchEvent(widget, buffer, event)
            }
        }

//        addTextChangedListener {
//            val charSequence = this.text
//            if (charSequence is Spannable) {
//                val spans = charSequence.getSpans(0, charSequence.length, URLSpan::class.java)
//                spans.filter { it.url.startsWith("tel:") && !it.toString().isTel() }
//                    .forEach { span ->
//                        val start = charSequence.getSpanStart(span)
//                        val end = charSequence.getSpanEnd(span)
//                        val spanText = charSequence.substring(start, end)
//                        if (!spanText.isTel()) {
//                            charSequence.removeSpan(span)
//                        }
//                    }
//            }
//        }
    }
}

fun TextView.measureLineCount(
    measuredWidth: Float,
    text: String?,
    max: Int = Integer.MAX_VALUE
): Int {
    if (text.isNullOrBlank() || measuredWidth <= 0f) {
        return 0
    }

    var lineCount = 0

    text.split("\n").forEach { line ->
        var substring = line
        if (substring.isBlank()) {
            lineCount++
        } else {
            while (substring.isNotBlank()) {
                val breakText = paint.breakText(substring, true, measuredWidth, null)
                if (breakText > 0) lineCount++
                substring = substring.substring(breakText)

                if (max <= lineCount) break
            }
        }

        if (max <= lineCount) return@forEach
    }

    return lineCount
}