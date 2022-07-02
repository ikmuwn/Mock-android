package kim.uno.mock.ui.retrofit

import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kim.uno.mock.data.remote.github.dto.GithubRepository
import kim.uno.mock.databinding.RecyclerViewFragmentBinding
import kim.uno.mock.extension.DefaultRecyclerViewAnimator
import kim.uno.mock.extension.addInitializationAnimator
import kim.uno.mock.extension.autoCleared
import kim.uno.mock.extension.observe
import kim.uno.mock.ui.BaseFragment
import kim.uno.mock.util.recyclerview.RecyclerViewAdapter

@AndroidEntryPoint
class RetrofitFragment : BaseFragment() {

    private var binding by autoCleared<RecyclerViewFragmentBinding>()
    private val viewModel by viewModels<RetrofitViewModel>()

    private val adapter = RecyclerViewAdapter.Builder()
        .addHolder(holder = GithubRepositoryHolder::class)
        .addBinder { holder, position, itemCount ->
            if (position == itemCount - 10) {
                viewModel.loadMore()
            }
        }
        .build()

    override fun onCreateViewOnce(inflater: LayoutInflater): View {
        binding = RecyclerViewFragmentBinding.inflate(inflater)
        observeActivityViewModel(viewModel)
        observe(viewModel.repositories, this::repositories)
        binding.recyclerView.adapter = adapter
        viewModel.refresh()
        adapter.recyclerView.addInitializationAnimator(DefaultRecyclerViewAnimator())
        return binding.root
    }

    private fun repositories(repositories: ArrayList<GithubRepository>) {
        adapter.notifyDataSetChange {
            it.clear()
            it.addAll(items = repositories)
        }
    }

}
