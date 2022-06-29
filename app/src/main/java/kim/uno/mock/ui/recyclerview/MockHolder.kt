package kim.uno.mock.ui.recyclerview

import kim.uno.mock.R
import kim.uno.mock.data.local.room.mock.MockEntity
import kim.uno.mock.databinding.MockHolderBinding
import kim.uno.mock.util.recyclerview.RecyclerViewAdapter

class MockHolder(adapter: RecyclerViewAdapter) :
    RecyclerViewAdapter.ViewHolder<MockEntity>(adapter, R.layout.mock_holder) {

    private val binding = MockHolderBinding.bind(itemView)

    override fun onBindView(item: MockEntity, position: Int) {
        super.onBindView(item, position)
        binding.item = item
    }

}