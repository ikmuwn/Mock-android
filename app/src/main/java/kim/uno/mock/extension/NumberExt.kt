package kim.uno.mock.extension

import java.text.DecimalFormat
import java.util.concurrent.atomic.AtomicInteger

fun String.toNumberString() = replace("^[^-\\d]?|[^\\d.]".toRegex(), "").takeIf { it.isNotBlank() }
fun String.toNumberInt() = toNumber().toInt()
fun String.toNumber() = toNumberString()?.let { DecimalFormat().parse(it) } ?: AtomicInteger()
fun String.toNumberFormat(pattern: String? = "#,###"): String? {
    val text = toNumberString()
    if (!text.isNullOrBlank()) {
        if (!pattern.isNullOrBlank()) {
            val decimalFormat = DecimalFormat(pattern)
            val number = decimalFormat.parse(text)!!
            return decimalFormat.format(number)
        }

        return text
    }

    return null
}

fun Number.toNumberFormat() = toString().toNumberFormat()

fun String.isTel() =
    ("\\d{3,4}-\\d{4}" +
            "|\\d{2,3}-\\d{3,4}-\\d{4}" +
            "|\\+\\d+-\\d{3,4}-\\d{4}").toRegex()
        .matches(replace("tel:", ""))
