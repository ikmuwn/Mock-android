package kim.uno.mock.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kim.uno.mock.ui.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor() : BaseViewModel() {

    private val _menuList = MutableLiveData<List<Menu>>()
    val menuList: LiveData<List<Menu>>
        get() = _menuList

    init {
        viewModelScope.launch {
            _menuList.value = listOf(
                Menu(
                    name = "RecyclerView Adapter",
                    directions = MenuFragmentDirections.actionMenuFragmentToRecyclerViewFragment()
                ),
                Menu(
                    name = "RecyclerView Adapter Builder",
                    directions = MenuFragmentDirections.actionMenuFragmentToRecyclerViewBuilderFragment()
                ),
                Menu(
                    name = "RecyclerView Draggable",
                    directions = MenuFragmentDirections.actionMenuFragmentToRecyclerViewDraggableFragment()
                ),
                Menu(
                    name = "RecyclerView Infinite loop",
                    directions = MenuFragmentDirections.actionMenuFragmentToRecyclerViewInfiniteLoopFragment()
                ),
                Menu(
                    name = "Paging (Pagination)",
                    directions = MenuFragmentDirections.actionMenuFragmentToPagingFragment()
                ),
                Menu(
                    name = "Retrofit with pagination",
                    directions = MenuFragmentDirections.actionMenuFragmentToRetrofitFragment()
                )
            )
        }
    }

}
