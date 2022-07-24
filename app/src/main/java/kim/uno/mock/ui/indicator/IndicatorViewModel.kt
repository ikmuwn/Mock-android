package kim.uno.mock.ui.indicator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kim.uno.mock.data.DataRepository
import kim.uno.mock.data.local.room.mock.MockEntity
import kim.uno.mock.ui.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IndicatorViewModel @Inject constructor(
    private val dataRepository: DataRepository
) : BaseViewModel() {

    private val _mockList = MutableLiveData<List<MockEntity>>()
    val mockList: LiveData<List<MockEntity>>
        get() = _mockList

    init {
        viewModelScope.launch {
            try {
                val mockList = dataRepository.getMockList(
                    size = 4,
                    postTime = null
                )
                _mockList.value = mockList
            } catch (e: Exception) {
                error(e)
            }
        }
    }

}
