package kim.uno.mock.ui.retrofit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kim.uno.mock.data.DataRepository
import kim.uno.mock.data.remote.github.dto.GithubRepository
import kim.uno.mock.ui.BaseViewModel
import kim.uno.mock.util.Paging
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RetrofitViewModel @Inject constructor(
    private val dataRepository: DataRepository
) : BaseViewModel() {

    private val _repositories = MutableLiveData<ArrayList<GithubRepository>>()
    val repositories: LiveData<ArrayList<GithubRepository>>
        get() = _repositories

    private val paging by lazy {
        Paging(
            first = 1,
            request = this::searchRepositories
        )
    }

    private val dummyQuery = "Android is robot"

    fun refresh() {
        paging.refresh()
    }

    fun loadMore() {
        paging.load()
    }

    private fun searchRepositories(page: Int) {
        viewModelScope.launch {
            showProgress()

            dataRepository.searchRepositories(
                query = dummyQuery,
                page = page
            ).success {
                it?.let {
                    if (paging.isFirstLoad) {
                        _repositories.value = it.items
                    } else {
                        val repositoriesMerge = ArrayList(_repositories.value!!)
                        repositoriesMerge.addAll(it.items)
                        _repositories.value = repositoriesMerge
                    }
                }

                paging.success(
                    next = page + 1,
                    endOfList = it?.items.isNullOrEmpty()
                )
            }.error {
                error(it)
                paging.error()
            }

            dismissProgress()
        }
    }

}
