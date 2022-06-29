package kim.uno.mock.extension

import androidx.lifecycle.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun <T> LifecycleOwner.observe(liveData: LiveData<T>, action: (t: T) -> Unit) {
    liveData.observe(this, Observer { action(it) })
}

fun <T> LifecycleOwner.observeAfterChanged(liveData: LiveData<T>, action: (t: T) -> Unit) {
    var initializedAlready = liveData.value != null
    liveData.observe(this, Observer {
        if (initializedAlready) {
            initializedAlready = false
        } else {
            action(it)
        }
    })
}

fun <T : LiveData<*>> ViewModel.debounceLiveData(liveData: T, duration: Long = 200L) =
    DebounceLiveData(this, liveData, duration) as T

class DebounceLiveData<T : LiveData<*>>(
    viewModel: ViewModel,
    liveData: T,
    duration: Long
) :
    MediatorLiveData<Any?>() {

    private var job: Job? = null

    init {
        addSource(liveData) {
            job?.cancel()
            job = viewModel.viewModelScope.launch {
                delay(duration)
                value = liveData.value
            }
        }
    }
}