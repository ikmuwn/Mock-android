package kim.uno.mock.ui.recyclerview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kim.uno.mock.data.DataRepository
import kim.uno.mock.data.local.room.mock.MockEntity
import kim.uno.mock.ui.BaseViewModel
import kim.uno.mock.util.Paging
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecyclerViewViewModel @Inject constructor(
    private val dataRepository: DataRepository
) : BaseViewModel() {

    private val _mockList = MutableLiveData<List<MockEntity>>()
    val mockList: LiveData<List<MockEntity>>
        get() = _mockList

    private val paging by lazy {
        Paging<Long?>(
            first = null,
            request = this::getMockList
        )
    }

    fun refresh() {
        paging.refresh()
    }

    fun loadMore() {
        paging.load()
    }

    private fun getMockList(postTime: Long?) {
        viewModelScope.launch {
            try {
                val mockList = dataRepository.getMockList(
                    size = 20,
                    postTime = postTime
                )

                if (paging.isFirstLoad) {
                    _mockList.value = mockList
                } else {
                    val notificationsMerge = ArrayList(_mockList.value!!)
                    notificationsMerge.addAll(mockList)
                    _mockList.value = notificationsMerge
                }

                paging.success(
                    next = mockList.last().postTime,
                    endOfList = mockList.size < 20
                )
            } catch (e: Exception) {
                paging.error()
            }
        }
    }

}
