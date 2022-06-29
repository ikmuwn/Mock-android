package kim.uno.mock.ui.recyclerview.fragment

import dagger.hilt.android.AndroidEntryPoint
import kim.uno.mock.data.local.room.mock.MockEntity
import kim.uno.mock.ui.recyclerview.MockAdapter
import kim.uno.mock.util.recyclerview.RecyclerViewAdapter

@AndroidEntryPoint
open class RecyclerViewFragment : AbstractRecyclerViewFragment() {

    override val adapter: RecyclerViewAdapter by lazy {
        MockAdapter()
    }

    override fun mockList(mockList: List<MockEntity>) {
        adapter.notifyDataSetChange {
            it.clear()
            it.addAll(items = mockList)
        }
    }

}