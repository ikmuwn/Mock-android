package kim.uno.mock.ui.recyclerview

import kim.uno.mock.R
import kim.uno.mock.data.local.room.mock.MockEntity
import kim.uno.mock.databinding.MockHolderBinding
import kim.uno.mock.util.recyclerview.DraggableRecyclerAdapter
import kim.uno.mock.util.recyclerview.RecyclerViewAdapter

class DraggableMockHolder(adapter: RecyclerViewAdapter) :
    RecyclerViewAdapter.ViewHolder<MockEntity>(adapter, R.layout.mock_holder),
    DraggableRecyclerAdapter.Draggable {

    private val binding = MockHolderBinding.bind(itemView)

    override fun onBindView(item: MockEntity, position: Int) {
        super.onBindView(item, position)
        binding.item = item
    }

    override fun onDragStateChanged(isSelected: Boolean) {
        super.onDragStateChanged(isSelected)
        binding.root.alpha = if (isSelected) 0.5f else 1f
    }

}
