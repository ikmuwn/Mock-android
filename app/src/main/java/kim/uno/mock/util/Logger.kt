package kim.uno.mock.util

import android.util.Log
import androidx.collection.ArrayMap
import kim.uno.mock.BuildConfig
import kotlin.math.max

class Logger(private var tag: String? = null, private val level: Int = Log.INFO) {

    private val headers = ArrayList<String>()
    private val lines = ArrayList<String>()
    private val footers = ArrayList<String>()

    fun header(line: String?): Logger {
        if (line.isNullOrEmpty()) return this

        headers.add("  $line")
        return this
    }

    fun footer(line: String?): Logger {
        if (line.isNullOrEmpty()) return this

        footers.add("  $line")
        return this
    }

    fun footer(map: ArrayMap<String, String>?): Logger {
        toJson(map)?.let { footers.add("  $it") }
        return this
    }

    fun line(line: String?, breakWord: Boolean = false): Logger {
        if (line.isNullOrEmpty()) return this

        // break word
        if (breakWord && line.length > OUTLINE.length) {
            var splitPoint = OUTLINE.length
            for (i in 1 until 20) {
                val index = OUTLINE.length - i
                if (line[OUTLINE.length - i] == ' ') {
                    splitPoint = index
                    break
                }
            }

            val restLine = line.substring(splitPoint + 1)
            val line = line.substring(0, splitPoint)

            lines.add("│ $line")
            line(restLine, breakWord = breakWord)
        } else {
            lines.add("│ $line")
        }
        return this
    }

    fun section(): Logger {
        lines.add(lines.size, SECTION)
        return this
    }

    fun table(map: ArrayMap<String, String>?): Logger {
        if (map.isNullOrEmpty()) return this

        var columnWidth = 0
        val keys = ArrayList<String>()
        for (key in map.keys) {
            keys.add(key)
            columnWidth = max(key.length, columnWidth)
        }

        for (key in keys.sorted()) {
            val value = map[key]
            if (!value.isNullOrEmpty()) {
                val values = map[key]!!.split("\n")
                for (i in values.indices) {
                    line(String.format("%-${columnWidth}s %s", if (i == 0) key else " ", values[i]))
                }
            }
        }

        return this
    }

    fun json(map: Map<String, String>?): Logger {
        toJson(map)?.let { line(it) }
        return this
    }

    private fun toJson(map: Map<String, String>?): String? {
        if (map.isNullOrEmpty()) return null

        var json = ""
        for (key in map.keys.sorted()) {
            json += if (json.isBlank()) "" else ", "
            json += "$key=${map[key]}"
        }
        return "{ $json }"
    }

    fun throwable(throwable: Throwable?): Logger {
        if (throwable == null) return this
        line(throwable.stackTraceToString())
        return this
    }

    fun show() {
        if (!BuildConfig.DEBUG) {
            return
        }

        val stackTrace = Thread.currentThread().stackTrace

        for (i in 2 until stackTrace.size) {
            if (!stackTrace[i].className.startsWith(javaClass.name)) {
                if (tag.isNullOrBlank()) {
                    tag = "$TAG/${stackTrace[i].fileName}"
                }
                headers.add(
                    0,
                    "${stackTrace[i].methodName} (${stackTrace[i].fileName}:${stackTrace[i].lineNumber})"
                )
                break
            }
        }

        var message = ""
        if (headers.size > 0) {
            message += headers.joinToString("\n")
        }

        if (lines.isNotEmpty()) {
            message += "\n$TOP_OUTLINE\n"
            message += lines.joinToString("\n") + "\n"
            message += "$BOTTOM_OUTLINE"
        }

        if (footers.size > 0) {
            message += "\n${footers.joinToString("\n")}"
        }

        when (level) {
            Log.VERBOSE -> Log.v(tag, message)
            Log.DEBUG -> Log.d(tag, message)
            Log.INFO -> Log.i(tag, message)
            Log.WARN -> Log.w(tag, message)
            Log.ERROR -> Log.e(tag, message)
        }
    }

    companion object {
        private const val OUTLINE =
            "────────────────────────────────────────────────────────────────────────────────────────────────────────────────"
        private const val SECTION_LINE =
            "┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄"
        private const val TOP_OUTLINE = "┌$OUTLINE"
        private const val SECTION = "├$SECTION_LINE"
        private const val BOTTOM_OUTLINE = "└$OUTLINE"
        private const val TAG = "NCenter"

        fun v(tag: String? = null, message: String? = null, throwable: Throwable? = null) =
            Logger(tag = tag, level = Log.VERBOSE).line(message).throwable(throwable).show()

        fun d(tag: String? = null, message: String? = null, throwable: Throwable? = null) =
            Logger(tag = tag, level = Log.DEBUG).line(message).throwable(throwable).show()

        fun i(tag: String? = null, message: String? = null, throwable: Throwable? = null) =
            Logger(tag = tag, level = Log.INFO).line(message).throwable(throwable).show()

        fun w(tag: String? = null, message: String? = null, throwable: Throwable? = null) =
            Logger(tag = tag, level = Log.WARN).line(message).throwable(throwable).show()

        fun e(tag: String? = null, message: String? = null, throwable: Throwable? = null) =
            Logger(tag = tag, level = Log.ERROR).line(message).throwable(throwable).show()
    }

}