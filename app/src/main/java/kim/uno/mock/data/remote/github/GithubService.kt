package kim.uno.mock.data.remote.github

import kim.uno.mock.data.remote.github.dto.GithubSearchResults
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubService {

    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String,
        @Query("page") page: Int
    ): Response<GithubSearchResults>

}
