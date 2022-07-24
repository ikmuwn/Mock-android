package kim.uno.mock.ui.recyclerview

import android.view.Gravity
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import kim.uno.mock.R
import kim.uno.mock.data.local.room.mock.MockEntity
import kim.uno.mock.databinding.MockHolderBinding
import kim.uno.mock.util.recyclerview.RecyclerViewAdapter

class MockHolder(adapter: RecyclerViewAdapter) :
    RecyclerViewAdapter.ViewHolder<MockEntity>(adapter, R.layout.mock_holder) {

    private val binding = MockHolderBinding.bind(itemView)

    init {

        // for indicator fragment
        if (adapter.recyclerView.parent is ViewPager2) {
            binding.root.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            binding.message.gravity = Gravity.CENTER
        }
    }

    override fun onBindView(item: MockEntity, position: Int) {
        super.onBindView(item, position)
        binding.item = item
    }

}
