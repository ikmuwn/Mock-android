package kim.uno.mock.data.remote.github

import kim.uno.mock.data.remote.GithubRetrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GithubData @Inject constructor(private val githubRetrofit: GithubRetrofit) {

    private val service by lazy { githubRetrofit.createService(GithubService::class.java) }

    suspend fun searchRepositories(
        query: String,
        page: Int
    ) = service.searchRepositories(
        query = query,
        page = page
    )

}
