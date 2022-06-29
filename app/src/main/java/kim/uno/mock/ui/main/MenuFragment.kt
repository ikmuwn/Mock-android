package kim.uno.mock.ui.main

import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kim.uno.mock.databinding.RecyclerViewFragmentBinding
import kim.uno.mock.extension.autoCleared
import kim.uno.mock.extension.observe
import kim.uno.mock.ui.BaseFragment
import kim.uno.mock.util.recyclerview.RecyclerViewAdapter

@AndroidEntryPoint
class MenuFragment : BaseFragment() {

    private var binding by autoCleared<RecyclerViewFragmentBinding>()
    private val viewModel by viewModels<MenuViewModel>()
    private val adapter by lazy {
        RecyclerViewAdapter.Builder()
            .addHolder(
                viewType = 0,
                holder = MenuHolder::class,
                viewModel::navigate
            )
            .build()
    }

    override fun onCreateViewOnce(inflater: LayoutInflater): View {
        binding = RecyclerViewFragmentBinding.inflate(inflater)
        observeActivityViewModel(viewModel)
        binding.recyclerView.adapter = adapter
        observe(viewModel.menuList, this::menuList)
        return binding.root
    }

    private fun menuList(menuList: List<Menu>) {
        adapter.notifyDataSetChange {
            it.clear()
            it.addAll(items = menuList)
        }
    }

}