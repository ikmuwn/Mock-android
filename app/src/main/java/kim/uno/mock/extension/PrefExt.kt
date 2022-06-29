package kim.uno.mock.extension

import android.content.Context
import android.content.SharedPreferences
import kim.uno.mock.BuildConfig

val Context.pref: SharedPreferences
    get() = getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)

fun SharedPreferences.put(key: String, value: Any?) = edit().let {
    when (value) {
        is Boolean -> it.putBoolean(key, value)
        is Float -> it.putFloat(key, value)
        is Int -> it.putInt(key, value)
        is Long -> it.putLong(key, value)
        is String -> it.putString(key, value)
    }
    it.commit()
}

fun SharedPreferences.remove(key: String) = edit().remove(key).commit()
fun SharedPreferences.clear() = edit().clear().commit()