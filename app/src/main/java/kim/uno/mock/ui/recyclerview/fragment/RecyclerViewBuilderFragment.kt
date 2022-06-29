package kim.uno.mock.ui.recyclerview.fragment

import dagger.hilt.android.AndroidEntryPoint
import kim.uno.mock.ui.recyclerview.MockHolder
import kim.uno.mock.util.recyclerview.RecyclerViewAdapter

@AndroidEntryPoint
class RecyclerViewBuilderFragment : RecyclerViewFragment() {

    override val adapter by lazy {
        RecyclerViewAdapter.Builder()
            .addHolder(holder = MockHolder::class)
            .build()
    }
}