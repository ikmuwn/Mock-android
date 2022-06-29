package kim.uno.mock.ui.paging

import dagger.hilt.android.AndroidEntryPoint
import kim.uno.mock.ui.recyclerview.MockHolder
import kim.uno.mock.ui.recyclerview.fragment.RecyclerViewFragment
import kim.uno.mock.util.recyclerview.RecyclerViewAdapter

@AndroidEntryPoint
class PagingFragment : RecyclerViewFragment() {

    override val adapter = RecyclerViewAdapter.Builder()
        .addHolder(holder = MockHolder::class)
        .addBinder { holder, position, itemCount ->
            if (position == itemCount - 10) {
                viewModel.loadMore()
            }
        }
        .build()

}