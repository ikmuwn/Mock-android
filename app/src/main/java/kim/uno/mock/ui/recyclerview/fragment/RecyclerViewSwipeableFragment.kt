package kim.uno.mock.ui.recyclerview.fragment

import dagger.hilt.android.AndroidEntryPoint
import kim.uno.mock.ui.recyclerview.SwipeableMockHolder
import kim.uno.mock.util.recyclerview.DraggableRecyclerAdapter

@AndroidEntryPoint
class RecyclerViewSwipeableFragment : RecyclerViewFragment() {

    override val adapter by lazy {
        DraggableRecyclerAdapter.Builder()
            .addHolder(holder = SwipeableMockHolder::class)
            .build()
    }

}
