package kim.uno.mock.util

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName

object GsonUtil {

    val gson: Gson by lazy { builder.create() }
    private val builder: GsonBuilder by lazy {
        GsonBuilder()
            .setExclusionStrategies(object : ExclusionStrategy {
                override fun shouldSkipField(f: FieldAttributes): Boolean {
                    return f.getAnnotation(SerializedName::class.java) == null
                }

                override fun shouldSkipClass(clazz: Class<*>?): Boolean {
                    return false
                }
            })
    }

}
