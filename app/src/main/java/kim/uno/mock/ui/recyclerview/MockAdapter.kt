package kim.uno.mock.ui.recyclerview

import kim.uno.mock.util.recyclerview.RecyclerViewAdapter

class MockAdapter : RecyclerViewAdapter() {

    override fun onCreateHolder(viewType: Int): ViewHolder<*> {
        return MockHolder(adapter = this)
    }

}