package kim.uno.mock.ui.retrofit

import kim.uno.mock.R
import kim.uno.mock.data.remote.github.dto.GithubRepository
import kim.uno.mock.databinding.GithubRepositoryHolderBinding
import kim.uno.mock.util.recyclerview.RecyclerViewAdapter

class GithubRepositoryHolder(adapter: RecyclerViewAdapter) :
    RecyclerViewAdapter.ViewHolder<GithubRepository>(adapter, R.layout.github_repository_holder) {

    private val binding = GithubRepositoryHolderBinding.bind(itemView)

    override fun onBindView(item: GithubRepository, position: Int) {
        super.onBindView(item, position)
        binding.item = item
    }

}
