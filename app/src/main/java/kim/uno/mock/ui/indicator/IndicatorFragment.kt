package kim.uno.mock.ui.indicator

import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kim.uno.mock.data.local.room.mock.MockEntity
import kim.uno.mock.databinding.IndicatorFragmentBinding
import kim.uno.mock.extension.autoCleared
import kim.uno.mock.extension.observe
import kim.uno.mock.ui.BaseFragment
import kim.uno.mock.ui.recyclerview.MockHolder
import kim.uno.mock.util.recyclerview.InfiniteRecyclerViewAdapter

@AndroidEntryPoint
class IndicatorFragment : BaseFragment() {

    private var binding by autoCleared<IndicatorFragmentBinding>()
    private val viewModel by viewModels<IndicatorViewModel>()
    private val adapter by lazy {
        InfiniteRecyclerViewAdapter.Builder()
            .addHolder(MockHolder::class)
            .build()
    }

    override fun onCreateViewOnce(inflater: LayoutInflater): View {
        binding = IndicatorFragmentBinding.inflate(inflater)
        observeActivityViewModel(viewModel)
        observe(viewModel.mockList, this::mockList)
        binding.viewPager.adapter = adapter

        binding.tabIndicator.viewPager = binding.viewPager
        binding.fillViewportTabIndicator.viewPager = binding.viewPager
        return binding.root
    }

    private fun mockList(mockList: List<MockEntity>) {
        adapter.notifyDataSetChange {
            it.clear()
            it.addAll(items = mockList)
        }
        binding.tabIndicator.setItems(mockList.mapIndexed { index, item -> "Page $index" }.toList())
        binding.fillViewportTabIndicator.setItems(mockList.mapIndexed { index, item -> "page $index" }.toList())
        binding.roundedBarIndicator.viewPager = binding.viewPager
    }

}
