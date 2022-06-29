package kim.uno.mock.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import kim.uno.mock.extension.debounceLiveData
import java.net.UnknownHostException

open class BaseViewModel : ViewModel() {

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean>
        get() = _progress

    val isProgress: Boolean
        get() = _progress.value == true

    val progressDebounce: LiveData<Boolean>
        get() = debounceLiveData(progress)

    private val _fatalError = MutableLiveData<Exception>()
    val fatalError: LiveData<Exception>
        get() = _fatalError

    private val _networkError = MutableLiveData<Exception>()
    val networkError: LiveData<Exception>
        get() = _networkError

    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception>
        get() = _error

    private val _directions = MutableLiveData<NavDirections>()
    val directions = debounceLiveData(
        liveData = _directions,
        duration = 50L
    )

    fun progress(visible: Boolean) {
        _progress.value = visible
    }

    fun showProgress() {
        progress(true)
    }

    fun dismissProgress() {
        progress(false)
    }

    fun fatalError(e: Exception) {
        _fatalError.value = e
    }

    fun networkError(e: Exception) {
        if (e is UnknownHostException) {
            _networkError.value = e
        } else {
            error(e)
        }
    }

    fun error(e: Exception) {
        _error.value = e
    }

    fun navigate(directions: NavDirections) {
        _directions.value = directions
    }

}