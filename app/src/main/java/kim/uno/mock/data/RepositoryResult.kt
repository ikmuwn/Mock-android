package kim.uno.mock.data

import android.util.Log
import kim.uno.mock.util.Logger

sealed class RepositoryResult<out T>(
    val data: T? = null,
    val e: Exception? = null
) {

    class Success<T>(data: T?) : RepositoryResult<T>(data = data)
    class Error(e: Exception) : RepositoryResult<Nothing>(e = e)

    override fun toString(): String {
        return when (this) {
            is Success -> "Success[data=$data]"
            is Error -> "Error[e=$e]"
        }
    }

    inline fun success(unit: (data: T?) -> Unit): RepositoryResult<T> {
        if (this is Success) {
            try {
                unit(data)
            } catch (e: Exception) {
                return Error(e)
            }
        }
        return this
    }

    inline fun error(unit: (e: Exception) -> Unit): RepositoryResult<T> {
        if (this is Error) {
            unit(e!!)
            Logger(level = Log.ERROR).throwable(e).show()
        }
        return this
    }

    inline fun finally(unit: () -> Unit): RepositoryResult<T> {
        unit()
        return this
    }

}