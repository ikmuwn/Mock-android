package kim.uno.mock.extension

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun Date.toFormat(format: String = "yyyyMMddHHmmss.SSS"): String {
    return SimpleDateFormat(format, Locale.ENGLISH).format(this)
}

fun Long.toFormat(format: String = "yyyy.MM.dd"): String {
    return Date(this).toFormat(format)
}

@SuppressLint("SimpleDateFormat")
fun String.toDate(format: String = "yyyyMMddHHmmss.SSS"): Date? {
    return SimpleDateFormat(format).parse(this)
}