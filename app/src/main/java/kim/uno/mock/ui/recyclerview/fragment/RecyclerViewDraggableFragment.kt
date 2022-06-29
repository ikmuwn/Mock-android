package kim.uno.mock.ui.recyclerview.fragment

import dagger.hilt.android.AndroidEntryPoint
import kim.uno.mock.ui.recyclerview.DraggableMockHolder
import kim.uno.mock.util.recyclerview.DraggableRecyclerAdapter

@AndroidEntryPoint
class RecyclerViewDraggableFragment : RecyclerViewFragment() {

    override val adapter by lazy {
        DraggableRecyclerAdapter.Builder()
            .addHolder(holder = DraggableMockHolder::class)
            .build()
    }

}