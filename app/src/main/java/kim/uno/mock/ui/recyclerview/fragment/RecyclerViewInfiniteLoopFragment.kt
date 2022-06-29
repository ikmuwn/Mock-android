package kim.uno.mock.ui.recyclerview.fragment

import dagger.hilt.android.AndroidEntryPoint
import kim.uno.mock.ui.recyclerview.MockHolder
import kim.uno.mock.util.recyclerview.InfiniteRecyclerViewAdapter

@AndroidEntryPoint
class RecyclerViewInfiniteLoopFragment : RecyclerViewFragment() {

    override val adapter by lazy {
        InfiniteRecyclerViewAdapter.Builder()
            .addHolder(holder = MockHolder::class)
            .build()
    }

}