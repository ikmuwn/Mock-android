package kim.uno.mock.ui.recyclerview.fragment

import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import kim.uno.mock.data.local.room.mock.MockEntity
import kim.uno.mock.databinding.RecyclerViewFragmentBinding
import kim.uno.mock.extension.autoCleared
import kim.uno.mock.extension.observe
import kim.uno.mock.ui.BaseFragment
import kim.uno.mock.ui.recyclerview.RecyclerViewViewModel
import kim.uno.mock.util.recyclerview.RecyclerViewAdapter

abstract class AbstractRecyclerViewFragment : BaseFragment() {

    private var binding by autoCleared<RecyclerViewFragmentBinding>()
    protected val viewModel by viewModels<RecyclerViewViewModel>()
    abstract val adapter: RecyclerViewAdapter

    override fun onCreateViewOnce(inflater: LayoutInflater): View {
        binding = RecyclerViewFragmentBinding.inflate(inflater)
        observeActivityViewModel(viewModel)
        observe(viewModel.mockList, this::mockList)
        binding.recyclerView.adapter = adapter
        viewModel.refresh()
        return binding.root
    }

    abstract fun mockList(mockList: List<MockEntity>)

}
