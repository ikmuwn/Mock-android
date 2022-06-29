package kim.uno.mock.ui.main

import androidx.navigation.NavDirections
import kim.uno.mock.R
import kim.uno.mock.databinding.MenuHolderBinding
import kim.uno.mock.util.recyclerview.RecyclerViewAdapter

class MenuHolder(adapter: RecyclerViewAdapter, val directions: (NavDirections) -> Unit) :
    RecyclerViewAdapter.ViewHolder<Menu>(adapter, R.layout.menu_holder) {

    private val binding = MenuHolderBinding.bind(itemView)

    override fun onBindView(item: Menu, position: Int) {
        super.onBindView(item, position)
        binding.item = item
        binding.menu.setOnClickListener {
            directions(item.directions)
        }
    }

}